<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Grocery Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    
    <div class="hero-section">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1 class="display-4 fw-bold text-primary">Fresh Groceries Delivered</h1>
                    <p class="lead">Get the freshest groceries delivered right to your doorstep. Shop from a wide variety of products with the best quality and prices.</p>
                    <div class="mt-4">
                        <c:choose>
                            <c:when test="${sessionScope.loggedInUser != null}">
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg me-3">
                                    <i class="fas fa-shopping-cart"></i> Shop Now
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-lg me-3">
                                    <i class="fas fa-user-plus"></i> Get Started
                                </a>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-lg">
                                    <i class="fas fa-sign-in-alt"></i> Login
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="col-md-6">
                    <img src="https://images.pexels.com/photos/1435735/pexels-photo-1435735.jpeg?auto=compress&cs=tinysrgb&w=600" 
                         alt="Fresh Groceries" class="img-fluid rounded shadow">
                </div>
            </div>
        </div>
    </div>
    
    <div class="features-section py-5">
        <div class="container">
            <div class="row text-center">
                <div class="col-md-4 mb-4">
                    <div class="feature-card p-4">
                        <i class="fas fa-truck fa-3x text-primary mb-3"></i>
                        <h4>Fast Delivery</h4>
                        <p>Quick and reliable delivery service to your doorstep within hours.</p>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="feature-card p-4">
                        <i class="fas fa-leaf fa-3x text-success mb-3"></i>
                        <h4>Fresh Products</h4>
                        <p>Carefully selected fresh groceries from the best suppliers.</p>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="feature-card p-4">
                        <i class="fas fa-shield-alt fa-3x text-info mb-3"></i>
                        <h4>Quality Guaranteed</h4>
                        <p>100% quality assurance with money-back guarantee on all products.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>