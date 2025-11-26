import { safeFetchJson } from './api-utils.js';

document.addEventListener('DOMContentLoaded', function() {
    fetchLocations();

    document.getElementById('createLocationForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const name = document.getElementById('name').value;
        const address = document.getElementById('address').value;

        fetch('/api/admin/locations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: name, address: address })
        })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            fetchLocations(); // Refresh the table
        })
        .catch((error) => {
            console.error('Error:', error);
        });
    });
});

function fetchLocations() {
    safeFetchJson('/api/admin/locations', true)
        .then(data => {
            const tableBody = document.getElementById('locationsTableBody');
            tableBody.innerHTML = ''; // Clear existing rows
            data.forEach(location => {
                const row = `<tr>
                    <td>${location.id}</td>
                    <td>${location.name}</td>
                    <td>${location.address}</td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        })
        .catch(err => {
            console.error('Failed to load locations:', err);
        });
}