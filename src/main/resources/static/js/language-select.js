function toggleLanguageDropdown() {
  const dropdown = document.getElementById("languageDropdown");
  dropdown.classList.toggle("show");
}

// Close dropdown when clicking outside
window.addEventListener("click", function (e) {
  if (!e.target.matches(".language-btn")) {
    const dropdown = document.getElementById("languageDropdown");
    if (dropdown.classList.contains("show")) {
      dropdown.classList.remove("show");
    }
  }
});
