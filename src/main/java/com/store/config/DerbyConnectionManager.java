package com.store.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DerbyConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DerbyConnectionManager.class);
    private static DerbyConnectionManager instance;
    private BlockingQueue<Connection> connectionPool;
    private String url;
    private String username;
    private String password;
    private int maxPoolSize;
    private int minPoolSize;
    
    private DerbyConnectionManager() {
        try {
            loadProperties();
            initializePool();
            createTables();
        } catch (Exception e) {
            logger.error("Failed to initialize Derby Connection Manager", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    public static synchronized DerbyConnectionManager getInstance() {
        if (instance == null) {
            instance = new DerbyConnectionManager();
        }
        return instance;
    }
    
    private void loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("derby.properties")) {
            if (input == null) {
                throw new IOException("Unable to find derby.properties");
            }
            props.load(input);
        }
        
        String driver = props.getProperty("derby.driver");
        url = props.getProperty("derby.url");
        username = props.getProperty("derby.username", "");
        password = props.getProperty("derby.password", "");
        maxPoolSize = Integer.parseInt(props.getProperty("connection.pool.max.size", "20"));
        minPoolSize = Integer.parseInt(props.getProperty("connection.pool.initial.size", "5"));
        
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Derby driver not found", e);
        }
    }
    
    private void initializePool() throws SQLException {
        connectionPool = new LinkedBlockingQueue<>();
        for (int i = 0; i < minPoolSize; i++) {
            connectionPool.offer(createNewConnection());
        }
        logger.info("Connection pool initialized with {} connections", minPoolSize);
    }
    
    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    public Connection getConnection() throws SQLException {
        Connection connection = connectionPool.poll();
        if (connection == null || connection.isClosed()) {
            connection = createNewConnection();
        }
        return connection;
    }
    
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connectionPool.size() < maxPoolSize) {
                    connectionPool.offer(connection);
                } else {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Error releasing connection", e);
            }
        }
    }
    
    private void createTables() {
        String[] createTableQueries = {
            // Customer table
            """
            CREATE TABLE customer (
                id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                contact VARCHAR(20),
                address VARCHAR(500),
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (id)
            )
            """,
            
            // Login table
            """
            CREATE TABLE login (
                login_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                email VARCHAR(100) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('Customer', 'Admin')),
                status VARCHAR(20) NOT NULL CHECK (status IN ('Active', 'Inactive')),
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (login_id)
            )
            """,
            
            // Product table
            """
            CREATE TABLE product (
                product_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                name VARCHAR(200) NOT NULL,
                description VARCHAR(1000),
                company VARCHAR(100),
                price DECIMAL(10,2) NOT NULL,
                stock INTEGER NOT NULL DEFAULT 0,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (product_id)
            )
            """,
            
            // Order table
            """
            CREATE TABLE orders (
                order_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                customer_id INTEGER NOT NULL,
                order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(20) NOT NULL CHECK (status IN ('Cart', 'Placed', 'Processing', 'Shipped', 'Delivered', 'Cancelled')),
                total_amount DECIMAL(10,2) DEFAULT 0.00,
                PRIMARY KEY (order_id),
                FOREIGN KEY (customer_id) REFERENCES customer(id)
            )
            """,
            
            // Order Item table
            """
            CREATE TABLE order_item (
                order_item_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                order_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                price_at_order DECIMAL(10,2) NOT NULL,
                PRIMARY KEY (order_item_id),
                FOREIGN KEY (order_id) REFERENCES orders(order_id),
                FOREIGN KEY (product_id) REFERENCES product(product_id)
            )
            """
        };
        
        try (Connection conn = getConnection()) {
            for (String query : createTableQueries) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(query);
                    logger.info("Table created successfully");
                } catch (SQLException e) {
                    if (e.getSQLState().equals("X0Y32")) {
                        logger.info("Table already exists, skipping creation");
                    } else {
                        logger.error("Error creating table: {}", e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating tables", e);
        }
    }
    
    public void shutdown() {
        try {
            while (!connectionPool.isEmpty()) {
                Connection conn = connectionPool.poll();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            }
            
            // Shutdown Derby
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                if ("XJ015".equals(e.getSQLState())) {
                    logger.info("Derby shut down normally");
                } else {
                    logger.error("Derby did not shut down normally", e);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during shutdown", e);
        }
    }
}