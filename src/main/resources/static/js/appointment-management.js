document.addEventListener("DOMContentLoaded", function () {
  fetchLocationsForDropdowns();
  fetchServicesForDropdown();
  fetchAppointments(); // Initial fetch for all appointments

  document
    .getElementById("createAppointmentForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      const startTime = document.getElementById("startTime").value;
      const endTime = document.getElementById("endTime").value;
      const maxCapacity = document.getElementById("maxCapacity").value;
      const locationId = document.getElementById("locationId").value;
      const serviceId = document.getElementById("serviceId").value;

      fetch("/api/employee/appointments", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          startTime: startTime,
          endTime: endTime,
          maxCapacity: maxCapacity,
          locationId: locationId,
          serviceId: serviceId,
        }),
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("Success:", data);
          fetchAppointments(); // Refresh the table
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    });

  document
    .getElementById("locationFilter")
    .addEventListener("change", function () {
      const locationId = this.value;
      if (locationId) {
        fetchAppointments(locationId);
      } else {
        fetchAppointments();
      }
    });
});

function fetchLocationsForDropdowns() {
  fetch("/api/public/locations")
    .then((response) => response.json())
    .then((data) => {
      const locationDropdown = document.getElementById("locationId");
      const locationFilter = document.getElementById("locationFilter");
      locationDropdown.innerHTML = "";
      locationFilter.innerHTML =
        '<option value="" th:text="#{appointment.filter.allLocations}"></option>';
      data.forEach((location) => {
        const option = `<option value="${location.id}">${location.name}</option>`;
        locationDropdown.innerHTML += option;
        locationFilter.innerHTML += option;
      });
    });
}

function fetchServicesForDropdown() {
  fetch("/api/employee/services")
    .then((response) => response.json())
    .then((data) => {
      const serviceDropdown = document.getElementById("serviceId");
      serviceDropdown.innerHTML = "";
      data.forEach((service) => {
        const option = `<option value="${service.id}">${service.name}</option>`;
        serviceDropdown.innerHTML += option;
      });
    });
}

function fetchAppointments(locationId) {
  let url = "/api/employee/appointments/future"; // Default to future appointments
  if (locationId) {
    url = `/api/employee/appointments?locationId=${locationId}`;
  }
  fetch(url)
    .then((response) => response.json())
    .then((data) => {
      const tableBody = document.getElementById("appointmentsTableBody");
      tableBody.innerHTML = "";
      data.forEach((appt) => {
        const row = `<tr>
                            <td>${appt.id}</td>
                            <td>${new Date(
                              appt.startTime
                            ).toLocaleString()}</td>
                            <td>${new Date(appt.endTime).toLocaleString()}</td>
                            <td>${appt.maxCapacity}</td>
                            <td>${appt.availableSpots}</td>
                            <td>${appt.locationId}</td>
                            <td>${appt.serviceId}</td>
                            <td>${appt.serviceName}</td>
                        </tr>`;
        tableBody.innerHTML += row;
      });
    });
}
