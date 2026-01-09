<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Demo Bank | Premier Digital Banking</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-blue: #003366;
            --accent-blue: #007bff;
            --text-dark: #1a1a1a;
            --bg-gray: #f4f7f9;
        }

        * { box-sizing: border-box; }
        body, html { margin: 0; padding: 0; font-family: 'Inter', sans-serif; overflow-x: hidden; }

        /* --- Navigation --- */
        nav {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 8%;
            background: #fff;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            position: fixed;
            top: 0; width: 100%; z-index: 1001;
        }

        .logo { font-size: 1.4rem; font-weight: 700; color: var(--primary-blue); text-decoration: none; }

        /* Desktop Links */
        .nav-links { display: flex; gap: 25px; align-items: center; }
        .nav-links a { text-decoration: none; color: var(--text-dark); font-weight: 500; }

        /* Hamburger Icon */
        .hamburger {
            display: none;
            flex-direction: column;
            gap: 5px;
            cursor: pointer;
            z-index: 1002;
        }
        .hamburger span {
            display: block;
            width: 25px;
            height: 3px;
            background: var(--primary-blue);
            transition: 0.3s;
        }

        /* Mobile Menu Overlay */
        .mobile-menu {
            position: fixed;
            top: 0;
            right: -100%;
            width: 80%;
            height: 100vh;
            background: #fff;
            z-index: 1000;
            display: flex;
            flex-direction: column;
            padding: 100px 40px;
            gap: 20px;
            transition: 0.4s ease-in-out;
            box-shadow: -5px 0 15px rgba(0,0,0,0.1);
        }
        .mobile-menu.active { right: 0; }
        .mobile-menu a { font-size: 1.2rem; text-decoration: none; color: var(--text-dark); font-weight: 600; }

        /* --- Carousel --- */
        .carousel-container { position: relative; height: 85vh; margin-top: 60px; overflow: hidden; background: #000; }
        .slide {
            position: absolute; top: 0; left: 0; width: 100%; height: 100%;
            opacity: 0; transition: opacity 0.8s; display: flex; align-items: center; padding: 0 8%;
            background-size: cover; background-position: center;
        }
        .slide.active { opacity: 1; }
        .slide:nth-child(1) { background-image: linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.4)), url('https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&q=80&w=2070'); }
        .slide:nth-child(2) { background-image: linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.4)), url('https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?auto=format&fit=crop&q=80&w=2070'); }
        
        .slide-content { color: white; max-width: 600px; }
        .slide-content h1 { font-size: clamp(2rem, 5vw, 3.5rem); margin-bottom: 15px; }

        .btn-login { background: var(--accent-blue); color: white !important; padding: 12px 25px; border-radius: 6px; text-decoration: none; display: inline-block; }

        /* --- Modal --- */
        .modal { display: none; position: fixed; z-index: 2000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.7); backdrop-filter: blur(5px); overflow-y: auto; }
        .modal-content { background: #fff; margin: 5% auto; padding: 30px; width: 90%; max-width: 800px; border-radius: 12px; }
        .demo-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 15px; margin-top: 20px; }
        .demo-card { background: #f4f7f9; padding: 15px; border-radius: 8px; text-decoration: none; color: inherit; border-left: 4px solid var(--accent-blue); }

        /* Responsive Breakpoint */
        @media (max-width: 768px) {
            .nav-links { display: none; }
            .hamburger { display: flex; }
        }
    </style>
</head>
<body>

    <nav>
        <a href="#" class="logo">Digital Bank</a>
        <div class="nav-links">
            <a href="javascript:void(0)" onclick="openModal()">Demo Directory</a>
            <a href="/bank" class="btn-login">Login</a>
        </div>
        <div class="hamburger" id="hamburger" onclick="toggleMobileMenu()">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </nav>

    <div class="mobile-menu" id="mobileMenu">
        <a href="/bank">Login</a>
        <a href="javascript:void(0)" onclick="toggleMobileMenu(); openModal();">Demo Directory</a>
        <a href="/bank/signup">Open Account</a>
    </div>

    <section class="carousel-container">
        <div class="slide active">
            <div class="slide-content">
                <h1>Banking Without Borders.</h1>
                <p>Experience the next generation of financial freedom.</p>
                <a href="/bank/signup" class="btn-login">Join Now</a>
            </div>
        </div>
        <div class="slide">
            <div class="slide-content">
                <h1>Smart Wealth Management.</h1>
                <p>Tools designed to help you grow your future.</p>
                <a href="javascript:void(0)" onclick="openModal()" class="btn-login">View Demo</a>
            </div>
        </div>
    </section>

    <div id="demoModal" class="modal">
        <div class="modal-content">
            <span style="float:right; cursor:pointer; font-size:1.5rem;" onclick="closeModal()">&times;</span>
            <h2 style="color: var(--primary-blue);">Demo Directory</h2>
            <div class="demo-grid">
                <a href="https://blazedemo.com" class="demo-card"><h3>Travel WebSite</h3><p>Partner Integration: Flight Booking.</p></a>
                <a href="/query.html" class="demo-card"><h3>Database Demo</h3><p>Back-end data tools.</p></a>
                <a href="/guicardexample.html" class="demo-card"><h3>Dashboard</h3><p>Card-based UI overview.</p></a>
				<a href="/nestedtableexample1.html" class="demo-card"><h3>Financial Statement</h3><p>Detailed Ledger and Reporting.</p></a>
				<a href="/nestedtableexample.html" class="demo-card"><h3>Portfolio Dashboard</h3><p>Advanced Asset and Wealth.</p></a>
				<a href="/flow-demo.html" class="demo-card"><h3>Process Flow</h3><p>Process Visualization.</p></a>
				<a href="/sv-user-profile.html" class="demo-card"><h3>SV Query Directory</h3><p>Virtual Service SQL query example.</p></a>
				<a href="/sv-profile.html" class="demo-card"><h3>SV Query Profile</h3><p>SQL Profiling.</p></a>
				<div class="tomcat-info">
				    Server Instance: <%= application.getServerInfo() %>
				</div>
            </div>
        </div>
    </div>

    <script>
        // Hamburger Menu Toggle
        function toggleMobileMenu() {
            const menu = document.getElementById('mobileMenu');
            const hamburger = document.getElementById('hamburger');
            menu.classList.toggle('active');
            
            // Optional: Animate hamburger to X
            hamburger.children[0].style.transform = menu.classList.contains('active') ? 'rotate(45deg) translate(5px, 6px)' : 'none';
            hamburger.children[1].style.opacity = menu.classList.contains('active') ? '0' : '1';
            hamburger.children[2].style.transform = menu.classList.contains('active') ? 'rotate(-45deg) translate(5px, -6px)' : 'none';
        }

        // Carousel Logic
        let currentSlide = 0;
        const slides = document.querySelectorAll('.slide');
        setInterval(() => {
            slides[currentSlide].classList.remove('active');
            currentSlide = (currentSlide + 1) % slides.length;
            slides[currentSlide].classList.add('active');
        }, 5000);

        // Modal Logic
        function openModal() { document.getElementById("demoModal").style.display = "block"; }
        function closeModal() { document.getElementById("demoModal").style.display = "none"; }
        window.onclick = (e) => { if(e.target == document.getElementById("demoModal")) closeModal(); }
    </script>

</body>
</html>