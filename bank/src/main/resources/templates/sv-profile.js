async function fetchProfile(userId) {
  try {
    // Example URL: replace with your real endpoint
    const response = await fetch(`https://vs263007svc416093.mock.blazemeter.com/profile/1`);
    if (!response.ok) throw new Error('Failed to fetch profile');
    const data = await response.json();
    renderProfile(data);
  } catch (err) {
    console.error(err);
    alert('Error loading profile');
  }
}

function renderProfile(data) {
  const { user, account } = data;

  // User fields
  document.getElementById("userId").textContent = user.id;
  document.getElementById("userName").textContent = `${user.firstname} ${user.lastname}`;
  document.getElementById("userEmail").textContent = user.email;
  document.getElementById("userPhone").textContent = user.phone;
  document.getElementById("userRole").textContent = user.role;

  // Account fields
  document.getElementById("accountId").textContent = account.accountId;
  document.getElementById("accountName").textContent = account.accountName;
  document.getElementById("accountIndustry").textContent = account.industry;
  document.getElementById("accountStatus").textContent = account.status;
  document.getElementById("accountCreated").textContent = account.createdAt;
  document.getElementById("accountCountry").textContent = account.country;
}

// Load profile for user with ID 1
fetchProfile(1);