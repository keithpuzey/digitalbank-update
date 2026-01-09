<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Demo Global Bank | Premier Digital Banking</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-blue: #003366;
            --accent-blue: #007bff;
            --text-dark: #1a1a1a;
            --text-light: #f8f9fa;
            --bg-gray: #f4f7f9;
        }

        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-gray);
            color: var(--text-dark);
            scroll-behavior: smooth;
        }

        /* --- Navigation --- */
        nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 8%;
            background: #fff;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            position: fixed;
            top: 0;
            width: 84%;
            z-index: 1000;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--primary-blue);
            text-decoration: none;
            letter-spacing: -1px;
        }

        .nav-links {
            display: flex;
            gap: 25px;
            align-items: center;
        }

        .nav-links a {
            text-decoration: none;
            color: var(--text-dark);
            font-weight: 500;
            transition: 0.3s;
        }

        .nav-links a:hover { color: var(--accent-blue); }

        /* Navigation Buttons */
        .btn-demo-trigger {
            cursor: pointer;
            border: 1px solid var(--primary-blue);
            padding: 10px 20px;
            border-radius: 6px;
        }

        .btn-login {
            background-color: var(--primary-blue);
            color: white !important;
            padding: 10px 25px;
            border-radius: 6px;
            box-shadow: 0 4px 12px rgba(0,51,102,0.2);
        }

        /* --- Hero Section --- */
        .hero {
            height: 80vh;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 8%;
            margin-top: 80px;
            background: linear-gradient(rgba(255,255,255,0.9), rgba(255,255,255,0.8)), 
                        url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&q=80&w=2070');
            background-size: cover;
            background-position: center;
        }

        .hero-text { max-width: 600px; }
        .hero-text h1 { font-size: 3.5rem; color: var(--primary-blue); margin-bottom: 20px; line-height: 1.1; }
        .hero-text p { font-size: 1.2rem; color: #555; margin-bottom: 30px; }

        /* --- Demo Modal (The "Demo Links" Page) --- */
        .modal {
            display: none; 
            position: fixed;
            z-index: 2000;
            left: 0; top: 0;
            width: 100%; height: 100%;
            background-color: rgba(0,0,0,0.6);
            backdrop-filter: blur(5px);
        }

        .modal-content {
            background-color: #fff;
            margin: 5% auto;
            padding: 40px;
            width: 80%;
            max-width: 1000px;
            border-radius: 15px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.3);
            animation: slideDown 0.4s ease-out;
        }

        @keyframes slideDown {
            from { transform: translateY(-50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }

        .close-modal {
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
            color: #aaa;
        }

        /* Grid Layout for Demo Links */
        .demo-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }

        .demo-card {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 10px;
            border-left: 4px solid var(--accent-blue);
            transition: 0.3s;
            text-decoration: none;
            color: inherit;
        }

        .demo-card:hover {
            background: #fff;
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.05);
        }

        .demo-card h3 { margin: 0 0 10px 0; color: var(--primary-blue); font-size: 1.1rem; }
        .demo-card p { margin: 0; font-size: 0.9rem; color: #666; }

        .tomcat-info {
            margin-top: 30px;
            font-size: 0.8rem;
            color: #999;
            text-align: center;
        }

        @media (max-width: 768px) {
            .hero-text h1 { font-size: 2.5rem; }
            nav { padding: 15px 5%; width: 90%; }
            .nav-links { gap: 10px; }
        }
    </style>
</head>
<body>

    <nav>
        <a href="#" class="logo">Digital Bank Demo</a>
        <div class="nav-links">
			<a href="/bank/signup" class="btn-login">Create Account</a>
            <a href="javascript:void(0)" class="btn-demo-trigger" onclick="openModal()">Demo Links</a>
            <a href="/bank" class="btn-login">Login</a>
        </div>
    </nav>

    <section class="hero">
        <div class="hero-text">
            <h1>The Future of Digital Banking is Here.</h1>
            <p>Secure, fast, and intuitive financial management for individuals and businesses worldwide.</p>
            <a href="/bank" style="text-decoration:none;" class="btn-login">Open Account</a>
        </div>
    </section>

    <div id="demoModal" class="modal">
        <div class="modal-content">
            <span class="close-modal" onclick="closeModal()">&times;</span>
            <h2 style="color: var(--primary-blue);">Demo Environment Directory</h2>
            <p>Select a module below to begin your demonstration.</p>

            <div class="demo-grid">
                <a href="/bank" class="demo-card">
                    <h3>Digital Banking</h3>
                    <p>Core consumer banking interface and dashboard.</p>
                </a>
                <a href="/query.html" class="demo-card">
                    <h3>Database Demo</h3>
                    <p>Interface for back-end data management.</p>
                </a>
                <a href="https://blazedemo.com/" target="_blank" class="demo-card">
                    <h3>Travel Website</h3>
                    <p>Partner integration: Flight booking engine.</p>
                </a>

                <a href="/nestedtableexample1.html" class="demo-card">
                    <h3>Financial Statement</h3>
                    <p>Detailed ledger and reporting view.</p>
                </a>
                <a href="/nestedtableexample.html" class="demo-card">
                    <h3>Portfolio Dashboard</h3>
                    <p>Advanced asset and wealth management.</p>
                </a>
                <a href="/guicardexample.html" class="demo-card">
                    <h3>Financial Dashboard</h3>
                    <p>Card-based financial overview UI.</p>
                </a>

                <a href="/flow-demo.html" class="demo-card">
                    <h3>Process Flow</h3>
                    <p>Visualization of transaction workflows.</p>
                </a>
                <a href="/sv-user-profile.html" class="demo-card">
                    <h3>SV Query Directory</h3>
                    <p>Virtual Service SQL directory management.</p>
                </a>
                <a href="/sv-profile.html" class="demo-card">
                    <h3>SV Query Profile</h3>
                    <p>SQL profiling and service virtualization.</p>
                </a>
            </div>

            <div class="tomcat-info">
                Server Instance: <%= application.getServerInfo() %>
            </div>
        </div>
    </div>

    <script>
        function openModal() {
            document.getElementById("demoModal").style.display = "block";
        }
        function closeModal() {
            document.getElementById("demoModal").style.display = "none";
        }
        // Close modal if user clicks outside of it
        window.onclick = function(event) {
            let modal = document.getElementById("demoModal");
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    </script>

</body>
</html>