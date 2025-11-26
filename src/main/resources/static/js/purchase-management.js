import { safeFetchJson } from './api-utils.js';

document.addEventListener("DOMContentLoaded", function () {
  fetchAvailablePurchases();
});

function fetchAvailablePurchases() {
  safeFetchJson("/api/member/purchases/available", true)
    .then((data) => {
      const tableBody = document.getElementById("purchasesTableBody");
      tableBody.innerHTML = ""; // Clear existing rows
      data.forEach((purchase) => {
        const row = `<tr>
                            <td>${purchase.id}</td>
                            <td>${purchase.serviceId}</td>
                            <td>${purchase.serviceName}</td>
                            <td>${purchase.amount}</td>
                            <td>${purchase.status}</td>
                            <td>${new Date(
                              purchase.purchasedAt
                            ).toLocaleString()}</td>
                        </tr>`;
        tableBody.innerHTML += row;
      });
    })
    .catch((err) => {
      console.error('Failed to load purchases:', err);
    });
}
