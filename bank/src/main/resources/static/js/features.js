document.addEventListener('DOMContentLoaded', function () {

  document.querySelectorAll('.feature-toggle').forEach(function (link) {
    link.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();                       // keep the dropdown open

      fetch(link.dataset.url, { method: 'POST', credentials: 'same-origin' })
        .then(function (r) {
          if (!r.ok) throw new Error('HTTP ' + r.status);
          return r.json();
        })
        .then(function (res) {
          var icon = link.querySelector('.feature-state');
          icon.classList.toggle('fa-check-square-o', res.enabled);
          icon.classList.toggle('fa-square-o', !res.enabled);
          link.style.fontWeight = res.enabled ? '600' : 'normal';
          if (!res.enabled) {
            document.querySelectorAll('.new-badge').forEach(function (b) { b.remove(); });
          }
        })
        .catch(function (err) {
          console.error('Feature toggle failed:', err);
          alert('Could not toggle feature (' + err.message + ')');
        });
    });
  });

  document.querySelectorAll('.new-badge[data-remaining-ms]').forEach(function (b) {
    setTimeout(function () { b.remove(); }, parseInt(b.dataset.remainingMs, 10));
  });
});