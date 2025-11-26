// Make sure to include SockJS and STOMP js libraries in your html file.
// For example:
// <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
// <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

document.addEventListener("DOMContentLoaded", function () {
  connect();
  fetchMyReservations();
  fetchFutureAppointments();
  fetchAvailablePurchases();

  document
    .getElementById("reserveAppointmentForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const appointmentId = document.getElementById("appointmentId").value;
      const purchaseId = document.getElementById("purchaseId").value;

      fetch("/api/member/reserve", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          appointmentId: appointmentId,
          purchaseId: purchaseId,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Success:", data);
          fetchMyReservations(); // Refresh the reservations table
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    });
});

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/reservations', function (seatUpdate) {
            showRemainingSeats(JSON.parse(seatUpdate.body));
        });
    });
}

function showRemainingSeats(seatUpdate) {
    // You will need to add an element to display the remaining seats for each appointment.
    // For example: <span id="remainingSeats-${appointmentId}"></span>
    const remainingSeatsElement = document.getElementById(`remainingSeats-${seatUpdate.appointmentId}`);
    if (remainingSeatsElement) {
        remainingSeatsElement.innerText = seatUpdate.remainingSeats;
    }
}


function fetchMyReservations() {
  fetch("/api/member/reservations")
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.getElementById("reservationsTableBody");
      tableBody.innerHTML = "";
      data.forEach((res) => {
        const row = `<tr>
                            <td>${res.id}</td>
                            <td>${res.memberId}</td>
                            <td>${res.memberName}</td>
                            <td>${res.appointmentId}</td>
                            <td>${new Date(
                              res.reservedAt
                            ).toLocaleString()}</td>
                            <td>${res.serviceName}</td>
                            <td>${new Date(
                              res.appointmentStartTime
                            ).toLocaleString()}</td>
                        </tr>`;
        tableBody.innerHTML += row;
      });
    });
}

function fetchFutureAppointments() {
  fetch("/api/employee/appointments/future") // Assuming this endpoint is accessible to members
    .then((response) => response.json())
    .then((data) => {
      const appointmentDropdown = document.getElementById("appointmentId");
      appointmentDropdown.innerHTML = "";
      data.forEach((appt) => {
        const option = `<option value="${appt.id}">${
          appt.serviceName
        } at ${new Date(appt.startTime).toLocaleString()}</option>`;
        appointmentDropdown.innerHTML += option;
      });
    });
}

function fetchAvailablePurchases() {
  fetch("/api/member/purchases/available")
    .then((response) => response.json())
    .then((data) => {
      const purchaseDropdown = document.getElementById("purchaseId");
      purchaseDropdown.innerHTML = "";
      data.forEach((purchase) => {
        const option = `<option value="${purchase.id}">${purchase.serviceName} - ${purchase.amount}</option>`;
        purchaseDropdown.innerHTML += option;
      });
    });
}
