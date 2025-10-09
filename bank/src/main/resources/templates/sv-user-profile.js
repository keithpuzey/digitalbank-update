async function fetchUsers(keyword = "") {
       let url = `https://vs263007svc416093.mock.blazemeter.com/users?keyword=`;
      if (keyword) {
         url += `${encodeURIComponent(keyword)}`;
       }
       const response = await fetch(url);
       const data = await response.json();
       renderUsers(data.users);
   }

   function renderUsers(users) {
     const tbody = document.getElementById("usersTableBody");
     if (!users || users.length === 0) {
       tbody.innerHTML = `<tr><td colspan="6" class="text-center">No users found</td></tr>`;
       return;
     }
     tbody.innerHTML = users.map(user => `
       <tr>
         <td>${user.id}</td>
         <td>${user.accountId}</td>
         <td>${user.firstname} ${user.lastname}</td>
         <td>${user.email}</td>
         <td>${user.phone}</td>
         <td>${user.role}</td>
       </tr>
     `).join("");
   }

   // Search button handler
   document.getElementById("searchBtn").addEventListener("click", () => {
     const keyword = document.getElementById("keywordInput").value;
     fetchUsers(keyword);
   });

   // Enter key support
   document.getElementById("keywordInput").addEventListener("keyup", (event) => {
     if (event.key === "Enter") {
       document.getElementById("searchBtn").click();
     }
   });

   // Initial load
   fetchUsers();