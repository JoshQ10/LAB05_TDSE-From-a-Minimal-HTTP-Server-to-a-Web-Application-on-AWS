// Talks to the four hardcoded JSON services exposed by the Java server.
// Every call is asynchronous (fetch) so the page never reloads and stays
// responsive while a request -including the deliberately slow one- is
// still in flight on the server.

const resultEl = document.getElementById("result");
const errorEl = document.getElementById("error");
const loadingEl = document.getElementById("loading");

function showLoading() {
  loadingEl.hidden = false;
  errorEl.hidden = true;
}

function showResult(data) {
  loadingEl.hidden = true;
  errorEl.hidden = true;
  resultEl.textContent = JSON.stringify(data, null, 2);
}

function showError(message) {
  loadingEl.hidden = true;
  errorEl.hidden = false;
  errorEl.textContent = message;
}

async function callService(path) {
  showLoading();
  let response;
  try {
    response = await fetch(path);
  } catch (networkError) {
    // fetch only rejects on network failure (DNS, offline, CORS) --
    // never on HTTP error statuses like 400 or 404.
    showError("Network error: could not reach the server. " + networkError.message);
    return;
  }

  let payload;
  try {
    payload = await response.json();
  } catch (parseError) {
    showError("The server response was not valid JSON.");
    return;
  }

  if (!response.ok) {
    showError(payload.error || `Request failed with HTTP status ${response.status}.`);
    return;
  }

  showResult(payload);
}

document.getElementById("greeting-form").addEventListener("submit", (event) => {
  event.preventDefault();
  const name = document.getElementById("name-input").value;
  callService("/app/greeting?name=" + encodeURIComponent(name));
});

document.getElementById("square-form").addEventListener("submit", (event) => {
  event.preventDefault();
  const value = document.getElementById("value-input").value;
  callService("/app/square?value=" + encodeURIComponent(value));
});

document.getElementById("time-button").addEventListener("click", () => {
  callService("/app/time");
});

document.getElementById("slow-button").addEventListener("click", () => {
  callService("/app/slow");
});
