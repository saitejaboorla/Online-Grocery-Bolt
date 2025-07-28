<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer class="bg-dark text-light py-4 mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h5><i class="fas fa-shopping-basket"></i> GroceryStore</h5>
                <p>Your trusted partner for fresh groceries and quality products.</p>
            </div>
            <div class="col-md-3">
                <h6>Quick Links</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/" class="text-light">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="text-light">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="text-light">Register</a></li>
                </ul>
            </div>
            <div class="col-md-3">
                <h6>Contact</h6>
                <p><i class="fas fa-envelope"></i> info@grocerystore.com</p>
                <p><i class="fas fa-phone"></i> +1 (555) 123-4567</p>
            </div>
        </div>
        <hr>
        <div class="text-center">
            <p>&copy; 2025 GroceryStore. All rights reserved.</p>
        </div>
    </div>
</footer>