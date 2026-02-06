<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Digital Bank | Premier Digital Banking</title>
<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">

<style>
/* --- Styles unchanged --- */
:root {
    --primary-blue: #003366;
    --accent-blue: #007bff;
    --text-dark: #1a1a1a;
    --bg-gray: #f4f7f9;
}
* { box-sizing: border-box; }
body, html { margin:0; padding:0; font-family:'Inter', sans-serif; overflow-x:hidden; }
nav{display:flex;justify-content:space-between;align-items:center;padding:15px 8%;background:#fff;box-shadow:0 2px 10px rgba(0,0,0,0.05);position:fixed;top:0;width:100%;z-index:1001;}
.logo{font-size:1.4rem;font-weight:700;color:var(--primary-blue);text-decoration:none;}
.nav-links{display:flex;gap:25px;align-items:center;}
.nav-links a{text-decoration:none;color:var(--text-dark);font-weight:500;}
.hamburger{display:none;flex-direction:column;gap:5px;cursor:pointer;z-index:1002;}
.hamburger span{display:block;width:25px;height:3px;background:var(--primary-blue);transition:0.3s;}
.mobile-menu{position:fixed;top:0;right:-100%;width:80%;height:100vh;background:#fff;z-index:1000;display:flex;flex-direction:column;padding:100px 40px;gap:20px;transition:0.4s ease-in-out;box-shadow:-5px 0 15px rgba(0,0,0,0.1);}
.mobile-menu.active{right:0;}
.mobile-menu a{font-size:1.2rem;text-decoration:none;color:var(--text-dark);font-weight:600;}
.carousel-container{position:relative;height:85vh;margin-top:60px;overflow:hidden;background:#000;touch-action:pan-y;}
.slide{position:absolute;top:0;left:0;width:100%;height:100%;opacity:0;transition:opacity 1.2s ease-in-out, transform 1.2s ease-in-out;display:flex;align-items:center;padding:0 8%;background-size:cover;background-position:center;transform:scale(1.05);}
.slide.active{opacity:1;transform:scale(1);z-index:1;}
.slide:nth-child(1){background-image:linear-gradient(rgba(0,0,0,0.4),rgba(0,0,0,0.4)),url('https://images.unsplash.com/photo-1563986768609-322da13575f3?auto=format&fit=crop&q=80&w=2070');}
.slide:nth-child(2){background-image:linear-gradient(rgba(0,0,0,0.4),rgba(0,0,0,0.4)),url('https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?auto=format&fit=crop&q=80&w=2070');}
.slide:nth-child(3){background-image:linear-gradient(rgba(0,0,0,0.4),rgba(0,0,0,0.4)),url('https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&q=80&w=2070');}
.slide-content{color:white;max-width:600px;z-index:2;}
.slide-content h1{font-size:clamp(2.2rem,5vw,3.8rem);margin-bottom:15px;font-weight:700;line-height:1.1;}
.slide-content p{font-size:1.2rem;margin-bottom:25px;opacity:0.9;}
.carousel-nav{position:absolute;top:50%;width:100%;display:flex;justify-content:space-between;padding:0 3%;transform:translateY(-50%);z-index:10;pointer-events:none;}
.nav-btn{background:rgba(255,255,255,0.15);backdrop-filter:blur(5px);color:white;width:50px;height:50px;border-radius:50%;display:flex;align-items:center;justify-content:center;cursor:pointer;font-size:1.2rem;transition:0.3s;pointer-events:auto;border:1px solid rgba(255,255,255,0.2);}
.nav-btn:hover{background:var(--accent-blue);border-color:transparent;}
.carousel-indicators{position:absolute;bottom:30px;left:50%;transform:translateX(-50%);display:flex;gap:12px;z-index:10;}
.dot{width:10px;height:10px;background:rgba(255,255,255,0.3);border-radius:50%;cursor:pointer;transition:0.3s;}
.dot.active{background:#fff;width:30px;border-radius:10px;}
.btn-login{background:var(--accent-blue);color:white !important;padding:12px 28px;border-radius:6px;text-decoration:none;display:inline-block;font-weight:600;}
.modal{display:none;position:fixed;z-index:2000;left:0;top:0;width:100%;height:100%;background:rgba(0,0,0,0.7);backdrop-filter:blur(5px);overflow-y:auto;}
.modal-content{background:#fff;margin:5% auto;padding:35px;width:90%;max-width:850px;border-radius:15px;position:relative;}
.demo-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(250px,1fr));gap:20px;margin-top:25px;}
.demo-card{background:#f8fafc;padding:20px;border-radius:10px;text-decoration:none;color:inherit;border-left:5px solid var(--accent-blue);transition:0.2s;box-shadow:0 2px 5px rgba(0,0,0,0.05);}
.demo-card:hover{transform:translateY(-3px);box-shadow:0 5px 15px rgba(0,0,0,0.1);}
.demo-card h3{margin:0 0 10px 0;color:var(--primary-blue);}
.demo-card p{margin:0;font-size:0.9rem;color:#666;}
.tomcat-info{grid-column:1/-1;font-size:0.8rem;color:#999;margin-top:20px;text-align:center;}
@media(max-width:768px){.nav-links{display:none;}.hamburger{display:flex;}.carousel-nav{display:none;}}
</style>
</head>
<body>

<nav>
<a href="#" class="logo">Digital Bank</a>
<div class="nav-links">
<a href="javascript:void(0)" onclick="openModal()">Demo Directory</a>
<a href="/bank" class="btn-login">Login</a>
</div>
<div class="hamburger" id="hamburger">
<span></span><span></span><span></span>
</div>
</nav>

<div class="mobile-menu" id="mobileMenu">
<a href="/bank">Login</a>
<a href="javascript:void(0)" onclick="toggleMobileMenu(); openModal();">Demo Directory</a>
<a href="/bank/signup">Open Account</a>
</div>

<section class="carousel-container" id="mainCarousel">
<div class="slide active"><div class="slide-content"><h1>Banking Without Borders.</h1><p>Experience the next generation of financial freedom with global access.</p><a href="/bank/login" class="btn-login">Login</a></div></div>
<div class="slide"><div class="slide-content"><h1>Smart Wealth Management.</h1><p>Advanced AI-driven tools designed to help you grow your future today.</p><a href="javascript:void(0)" onclick="openModal()" class="btn-login">View Demo</a></div></div>
<div class="slide"><div class="slide-content"><h1>Secure by Design.</h1><p>Rest easy with biometric security and real-time fraud monitoring.</p><a href="/bank/signup" class="btn-login">Open Secure Account</a></div></div>

<div class="carousel-nav">
<div class="nav-btn" data-step="-1">&#10094;</div>
<div class="nav-btn" data-step="1">&#10095;</div>
</div>
<div class="carousel-indicators">
<div class="dot" data-index="0"></div>
<div class="dot" data-index="1"></div>
<div class="dot" data-index="2"></div>
</div>
</section>

<div id="demoModal" class="modal">
<div class="modal-content">
<span style="position:absolute;right:25px;top:20px;cursor:pointer;font-size:2rem;color:#999;" onclick="closeModal()">&times;</span>
<h2 style="color:var(--primary-blue);margin-top:0;">Demo Directory</h2>
<div class="demo-grid">
<a href="https://blazedemo.com" class="demo-card"><h3>Travel WebSite</h3><p>Partner Integration: Flight Booking.</p></a>
<a href="/query.html" class="demo-card"><h3>Database Demo</h3><p>Back-end data tools.</p></a>
<a href="/guicardexample.html" class="demo-card"><h3>Dashboard</h3><p>Card-based UI overview.</p></a>
<a href="/nestedtableexample1.html" class="demo-card"><h3>Financial Statement</h3><p>Detailed Ledger and Reporting.</p></a>
<a href="/nestedtableexample.html" class="demo-card"><h3>Portfolio Dashboard</h3><p>Advanced Asset and Wealth.</p></a>
<a href="/flow-demo.html" class="demo-card"><h3>Process Flow</h3><p>Process Visualization.</p></a>
<a href="/sv-user-profile.html" class="demo-card"><h3>SV Query Directory</h3><p>Virtual Service SQL query example.</p></a>
<a href="/sv-profile.html" class="demo-card"><h3>SV Query Profile</h3><p>SQL Profiling.</p></a>
<div class="tomcat-info">Server Instance: <%= application.getServerInfo() %></div>
</div>
</div>
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    // --- Mobile Menu ---
    const hamburger = document.getElementById('hamburger');
    const mobileMenu = document.getElementById('mobileMenu');
    if(hamburger && mobileMenu){
        window.toggleMobileMenu = function(){
            const isActive = mobileMenu.classList.toggle('active');
            hamburger.children[0].style.transform = isActive ? 'rotate(45deg) translate(5px,6px)' : 'none';
            hamburger.children[1].style.opacity = isActive ? '0':'1';
            hamburger.children[2].style.transform = isActive ? 'rotate(-45deg) translate(5px,-6px)' : 'none';
        };
        hamburger.addEventListener('click', toggleMobileMenu);
    }

    // --- Modal ---
    const modal = document.getElementById('demoModal');
    if(modal){
        window.openModal = ()=>{ modal.style.display='block'; document.body.style.overflow='hidden'; };
        window.closeModal = ()=>{ modal.style.display='none'; document.body.style.overflow='auto'; };
        window.addEventListener('click', e=>{ if(e.target===modal) closeModal(); });
    }

    // --- Carousel ---
    const carousel = document.getElementById('mainCarousel');
    if(carousel){
        const slides = carousel.querySelectorAll('.slide');
        const dots = carousel.querySelectorAll('.dot');
        const navBtns = carousel.querySelectorAll('.nav-btn');
        let currentIndex=0;
        let slideInterval=setInterval(nextSlide,7000);

        function updateUI(){ slides.forEach((s,i)=>s.classList.toggle('active',i===currentIndex)); dots.forEach((d,i)=>d.classList.toggle('active',i===currentIndex)); }
        function nextSlide(){ currentIndex=(currentIndex+1)%slides.length; updateUI(); }
        function moveSlide(step){ resetTimer(); currentIndex=(currentIndex+step+slides.length)%slides.length; updateUI(); }
        function setSlide(index){ resetTimer(); currentIndex=index; updateUI(); }
        function resetTimer(){ clearInterval(slideInterval); slideInterval=setInterval(nextSlide,7000); }

        dots.forEach(dot => dot.addEventListener('click', ()=>setSlide(parseInt(dot.dataset.index))));
        navBtns.forEach(btn => btn.addEventListener('click', ()=>moveSlide(parseInt(btn.dataset.step))));

        // --- Touch Swipe ---
        let touchStartX=0,touchEndX=0;
        carousel.addEventListener('touchstart', e=>touchStartX=e.changedTouches[0].screenX,{passive:true});
        carousel.addEventListener('touchend', e=>{
            touchEndX=e.changedTouches[0].screenX;
            const threshold=50;
            if(touchStartX-touchEndX>threshold) moveSlide(1);
            else if(touchEndX-touchStartX>threshold) moveSlide(-1);
        },{passive:true});
    }
});
</script>

</body>
</html>