/**
 * Fetch JSON and handle HTTP errors. Optionally normalize to an array.
 * @param {string} url
 * @param {boolean} expectArray - if true, returns [] when response shape is not an array
 * @returns {Promise<any>}
 */
export function safeFetchJson(url, expectArray = false) {
  return fetch(url).then((res) => {
    if (!res.ok) {
      return res.text().then((text) => {
        throw new Error(`HTTP ${res.status}: ${text}`);
      });
    }
    return res.json();
  }).then((data) => {
    if (!expectArray) return data;
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.content)) return data.content;
    if (Array.isArray(data?.employees)) return data.employees;
    return []; // safe fallback when expecting an array
  });
}