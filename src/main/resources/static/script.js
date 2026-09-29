/* =========================================================
   EXAMCRAFT - FRONTEND JAVASCRIPT
========================================================= */

const API = "/api";

/* =========================================================
   COMMON API FUNCTION
========================================================= */

async function apiRequest(url, options = {}) {
    try {
        const response = await fetch(API + url, {
            headers: {
                "Content-Type": "application/json"
            },
            ...options
        });

        const text = await response.text();

        let data = null;

        try {
            data = text ? JSON.parse(text) : null;
        } catch {
            data = text;
        }

        if (!response.ok) {
            let message = "Request failed";

            if (data && data.error) {
                message = data.error;
            } else if (data && typeof data === "object") {
                message = Object.values(data).join(", ");
            } else if (typeof data === "string" && data) {
                message = data;
            }

            throw new Error(message);
        }

        return data;

    } catch (error) {
        console.error("API Error:", error);
        throw error;
    }
}


/* =========================================================
   PAGE NAVIGATION
========================================================= */

function showPage(pageName) {

    document.querySelectorAll(".page").forEach(page => {
        page.classList.remove("active");
    });

    const target = document.getElementById("page-" + pageName);

    if (target) {
        target.classList.add("active");
    }

    document.querySelectorAll(".nav").forEach(button => {
        button.classList.remove("active");
    });

    const activeButton = document.querySelector(
        `.nav[data-page="${pageName}"]`
    );

    if (activeButton) {
        activeButton.classList.add("active");
    }

    const titles = {
        dashboard: "Dashboard",
        units: "Units",
        questions: "Question Bank",
        papers: "Test Papers",
        attempts: "Attempts",
        usage: "Usage Frequency"
    };

    const title = document.getElementById("page-title");

    if (title) {
        title.textContent = titles[pageName] || "ExamCraft";
    }

    const breadcrumb = document.getElementById("breadcrumb-page");

    if (breadcrumb) {
        breadcrumb.textContent = titles[pageName] || "ExamCraft";
    }

    /* Load page data */

    if (pageName === "dashboard") {
        loadDashboard();
    }

    if (pageName === "units") {
        loadUnits();
    }

    if (pageName === "questions") {
        loadQuestions();
    }

    if (pageName === "papers") {
        loadPapers();
    }

    if (pageName === "attempts") {
        loadAttempts();
    }

    if (pageName === "usage") {
        loadUsage();
    }
}


/* =========================================================
   NAVIGATION BUTTON EVENTS
========================================================= */

document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".nav").forEach(button => {

        button.addEventListener("click", () => {

            const page = button.dataset.page;

            if (page) {
                showPage(page);
            }
        });
    });

    loadDashboard();
    checkAPIStatus();

});


/* =========================================================
   API STATUS
========================================================= */

async function checkAPIStatus() {

    const apiElement = document.getElementById("api");

    try {

        await apiRequest("/units");

        if (apiElement) {

            apiElement.classList.remove("off");

            apiElement.innerHTML =
                "<i></i> API Connected";
        }

        const status = document.getElementById("status-text");

        if (status) {
            status.textContent = "API Connected";
        }

    } catch (error) {

        if (apiElement) {

            apiElement.classList.add("off");

            apiElement.innerHTML =
                "<i></i> API Offline";
        }

        const status = document.getElementById("status-text");

        if (status) {
            status.textContent = "API Offline";
        }
    }
}


/* =========================================================
   DASHBOARD
========================================================= */

async function loadDashboard() {

    try {

        const [
            units,
            questions,
            papers,
            attempts
        ] = await Promise.all([
            apiRequest("/units"),
            apiRequest("/questions"),
            apiRequest("/test-papers"),
            apiRequest("/attempts")
        ]);

        setText("total-units", units?.length || 0);
        setText("total-questions", questions?.length || 0);
        setText("total-papers", papers?.length || 0);
        setText("total-attempts", attempts?.length || 0);

    } catch (error) {

        console.error("Dashboard error:", error);

    }
}


/* =========================================================
   UNITS
========================================================= */

async function loadUnits() {

    const container = document.getElementById("units");

    if (!container) {
        console.error("Units container not found");
        return;
    }

    container.innerHTML =
        `<div class="loading">Loading units...</div>`;

    try {

        const units = await apiRequest("/units");

        if (!units || units.length === 0) {

            container.innerHTML =
                `<div class="empty">No units available.</div>`;

            return;
        }

        let html = `
            <div class="panel">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Unit Name</th>
                        </tr>
                    </thead>
                    <tbody>
        `;

        units.forEach(unit => {

            html += `
                <tr>
                    <td><b>#${unit.id}</b></td>
                    <td>${esc(unit.unitName)}</td>
                </tr>
            `;
        });

        html += `
                    </tbody>
                </table>
            </div>
        `;

        container.innerHTML = html;

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


/* =========================================================
   ADD UNIT
========================================================= */

async function addUnit(event) {

    event.preventDefault();

    const input = document.getElementById("unitName");

    if (!input) return;

    const unitName = input.value.trim();

    if (!unitName) {

        showToast("Please enter a unit name.", true);

        return;
    }

    try {

        await apiRequest("/units", {
            method: "POST",
            body: JSON.stringify({
                unitName: unitName
            })
        });

        input.value = "";

        closeModal("unitModal");

        showToast("Unit added successfully.");

        loadUnits();
        loadDashboard();

    } catch (error) {

        showToast(error.message, true);
    }
}


/* =========================================================
   QUESTIONS
========================================================= */

let allQuestions = [];


async function loadQuestions() {

    const container = document.getElementById("questions");

    if (!container) {
        console.error("Questions container not found");
        return;
    }

    container.innerHTML =
        `<div class="loading">Loading questions...</div>`;

    try {

        allQuestions = await apiRequest("/questions");

        renderQuestions(allQuestions);

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


/* =========================================================
   RENDER QUESTIONS
========================================================= */

function renderQuestions(questions) {

    const container = document.getElementById("questions");

    if (!container) return;

    if (!questions || questions.length === 0) {

        container.innerHTML =
            `<div class="empty">No questions available.</div>`;

        return;
    }

    let html = `
        <div class="panel">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Question</th>
                        <th>Topic</th>
                        <th>Difficulty</th>
                        <th>Unit</th>
                        <th>Answer</th>
                    </tr>
                </thead>
                <tbody>
    `;

    questions.forEach(question => {

        const difficulty =
            String(question.difficulty || "").toLowerCase();

        const unitName =
            question.unit
                ? question.unit.unitName
                : "—";

        html += `
            <tr>
                <td><b>#${question.id}</b></td>

                <td>
                    ${esc(question.questionText)}
                </td>

                <td>
                    ${esc(question.topic)}
                </td>

                <td>
                    <span class="badge ${difficulty}">
                        ${esc(question.difficulty)}
                    </span>
                </td>

                <td>
                    ${esc(unitName)}
                </td>

                <td>
                    <b>${esc(question.correctAnswer)}</b>
                </td>
            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
}


/* =========================================================
   QUESTION SEARCH
========================================================= */

function filterQuestions() {

    const searchInput =
        document.getElementById("questionSearch");

    const difficultySelect =
        document.getElementById("difficultyFilter");

    if (!searchInput) return;

    const search =
        searchInput.value.toLowerCase().trim();

    const difficulty =
        difficultySelect
            ? difficultySelect.value.toLowerCase()
            : "";

    const filtered = allQuestions.filter(question => {

        const text =
            `${question.questionText || ""} ${question.topic || ""}`
                .toLowerCase();

        const matchesSearch =
            text.includes(search);

        const matchesDifficulty =
            !difficulty ||
            String(question.difficulty || "").toLowerCase() === difficulty;

        return matchesSearch && matchesDifficulty;
    });

    renderQuestions(filtered);
}


/* =========================================================
   ADD QUESTION
========================================================= */

async function addQuestion(event) {

    event.preventDefault();

    const data = {

        questionText:
            valueOf("questionText"),

        topic:
            valueOf("topic"),

        difficulty:
            valueOf("difficulty"),

        optionA:
            valueOf("optionA"),

        optionB:
            valueOf("optionB"),

        optionC:
            valueOf("optionC"),

        optionD:
            valueOf("optionD"),

        correctAnswer:
            valueOf("correctAnswer"),

        unit: {
            id: Number(valueOf("questionUnit"))
        }
    };

    try {

        await apiRequest("/questions", {
            method: "POST",
            body: JSON.stringify(data)
        });

        closeModal("questionModal");

        showToast("Question added successfully.");

        document
            .getElementById("questionForm")
            ?.reset();

        loadQuestions();
        loadDashboard();

    } catch (error) {

        showToast(error.message, true);
    }
}


/* =========================================================
   LOAD UNIT OPTIONS
========================================================= */

async function loadUnitOptions() {

    try {

        const units = await apiRequest("/units");

        const select =
            document.getElementById("questionUnit");

        if (!select) return;

        select.innerHTML =
            `<option value="">Select Unit</option>`;

        units.forEach(unit => {

            select.innerHTML += `
                <option value="${unit.id}">
                    ${esc(unit.unitName)}
                </option>
            `;
        });

    } catch (error) {

        console.error("Could not load unit options:", error);
    }
}


/* =========================================================
   TEST PAPERS
========================================================= */

async function loadPapers() {

    const container = document.getElementById("papers");

    if (!container) {
        console.error("Papers container not found");
        return;
    }

    container.innerHTML =
        `<div class="loading">Loading test papers...</div>`;

    try {

        const papers =
            await apiRequest("/test-papers");

        renderPapers(papers);

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


/* =========================================================
   RENDER TEST PAPERS
========================================================= */

function renderPapers(papers) {

    const container = document.getElementById("papers");

    if (!container) return;

    if (!papers || papers.length === 0) {

        container.innerHTML =
            `<div class="empty">No test papers generated yet.</div>`;

        return;
    }

    let html = `<div class="papers-grid">`;

    papers.forEach(paper => {

        html += `
            <div class="paper">

                <span class="paper-id">
                    #${paper.id}
                </span>

                <h3>
                    ${esc(paper.title)}
                </h3>

                <small>
                    ${paper.totalQuestions} Questions
                </small>

                <div class="mix">

                    <span class="e">
                        Easy: ${paper.easyCount}
                    </span>

                    <span class="m">
                        Medium: ${paper.mediumCount}
                    </span>

                    <span class="h">
                        Hard: ${paper.hardCount}
                    </span>

                </div>

            </div>
        `;
    });

    html += `</div>`;

    container.innerHTML = html;
}


/* =========================================================
   GENERATE TEST PAPER
========================================================= */

async function generateTestPaper(event) {

    event.preventDefault();

    const totalQuestions =
        Number(valueOf("totalQuestions"));

    const easyCount =
        Number(valueOf("easyCount"));

    const mediumCount =
        Number(valueOf("mediumCount"));

    const hardCount =
        Number(valueOf("hardCount"));

    const title =
        valueOf("paperTitle");

    if (
        easyCount +
        mediumCount +
        hardCount !==
        totalQuestions
    ) {

        showToast(
            "Easy + Medium + Hard must equal Total Questions.",
            true
        );

        return;
    }

    const data = {

        title: title,

        totalQuestions: totalQuestions,

        easyCount: easyCount,

        mediumCount: mediumCount,

        hardCount: hardCount
    };

    try {

        await apiRequest("/test-papers/generate", {
            method: "POST",
            body: JSON.stringify(data)
        });

        closeModal("paperModal");

        showToast("Test paper generated successfully.");

        document
            .getElementById("paperForm")
            ?.reset();

        loadPapers();
        loadDashboard();

    } catch (error) {

        showToast(error.message, true);
    }
}


/* =========================================================
   ATTEMPTS
========================================================= */

async function loadAttempts() {

    const container = document.getElementById("attempts");

    if (!container) {
        console.error("Attempts container not found");
        return;
    }

    container.innerHTML =
        `<div class="loading">Loading attempts...</div>`;

    try {

        const attempts =
            await apiRequest("/attempts");

        renderAttempts(attempts);

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


/* =========================================================
   RENDER ATTEMPTS
========================================================= */

function renderAttempts(attempts) {

    const container =
        document.getElementById("attempts");

    if (!container) return;

    if (!attempts || attempts.length === 0) {

        container.innerHTML =
            `<div class="empty">No attempts available.</div>`;

        return;
    }

    let html = `
        <div class="panel">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Student</th>
                        <th>Score</th>
                        <th>Total Marks</th>
                        <th>Test Paper</th>
                    </tr>
                </thead>
                <tbody>
    `;

    attempts.forEach(attempt => {

        const paperTitle =
            attempt.testPaper
                ? attempt.testPaper.title
                : "—";

        html += `
            <tr>

                <td>
                    <b>#${attempt.id}</b>
                </td>

                <td>
                    ${esc(attempt.studentName)}
                </td>

                <td>
                    <b>${attempt.score}</b>
                </td>

                <td>
                    ${attempt.totalMarks}
                </td>

                <td>
                    ${esc(paperTitle)}
                </td>

            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
}


/* =========================================================
   USAGE FREQUENCY
========================================================= */

async function loadUsage() {

    const container =
        document.getElementById("usage");

    if (!container) {
        console.error("Usage container not found");
        return;
    }

    container.innerHTML =
        `<div class="loading">Loading usage frequency...</div>`;

    try {

        const usage =
            await apiRequest("/test-papers/usage-frequency");

        renderUsage(usage);

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


/* =========================================================
   RENDER USAGE FREQUENCY
========================================================= */

function renderUsage(usage) {

    const container =
        document.getElementById("usage");

    if (!container) return;

    if (!usage || Object.keys(usage).length === 0) {

        container.innerHTML =
            `<div class="empty">
                No question usage data available.
            </div>`;

        return;
    }

    let html = `
        <div class="panel">
            <table>

                <thead>
                    <tr>
                        <th>Question ID</th>
                        <th>Usage Count</th>
                    </tr>
                </thead>

                <tbody>
    `;

    Object.entries(usage).forEach(([questionId, count]) => {

        html += `
            <tr>

                <td>
                    <b>Question #${esc(questionId)}</b>
                </td>

                <td>
                    ${count}
                </td>

            </tr>
        `;
    });

    html += `
                </tbody>

            </table>
        </div>
    `;

    container.innerHTML = html;
}


/* =========================================================
   MODAL FUNCTIONS
========================================================= */

function openModal(id) {

    const modal =
        document.getElementById(id);

    if (!modal) return;

    modal.classList.remove("hidden");

    if (id === "questionModal") {
        loadUnitOptions();
    }
}


function closeModal(id) {

    const modal =
        document.getElementById(id);

    if (!modal) return;

    modal.classList.add("hidden");
}


/* =========================================================
   CLOSE MODAL WHEN CLICKING OUTSIDE
========================================================= */

document.addEventListener("click", event => {

    if (event.target.classList.contains("modal-overlay")) {

        const modal =
            event.target.closest(".modal");

        if (modal) {
            modal.classList.add("hidden");
        }
    }
});


/* =========================================================
   TOAST MESSAGE
========================================================= */

function showToast(message, isError = false) {

    const container =
        document.getElementById("toasts");

    if (!container) return;

    const toast =
        document.createElement("div");

    toast.className =
        "toast" + (isError ? " bad" : "");

    toast.textContent =
        message;

    container.appendChild(toast);

    setTimeout(() => {

        toast.remove();

    }, 3500);
}


/* =========================================================
   HELPER FUNCTIONS
========================================================= */

function setText(id, value) {

    const element =
        document.getElementById(id);

    if (element) {
        element.textContent = value;
    }
}


function valueOf(id) {

    const element =
        document.getElementById(id);

    return element
        ? element.value.trim()
        : "";
}


function esc(value) {

    return String(value ?? "").replace(
        /[&<>"']/g,
        function (match) {

            return {
                "&": "&amp;",
                "<": "&lt;",
                ">": "&gt;",
                '"': "&quot;",
                "'": "&#039;"
            }[match];

        }
    );
}


/* =========================================================
   KEYBOARD ESCAPE FOR MODALS
========================================================= */

document.addEventListener("keydown", event => {

    if (event.key === "Escape") {

        document
            .querySelectorAll(".modal")
            .forEach(modal => {

                modal.classList.add("hidden");

            });
    }
});


/* =========================================================
   REFRESH API STATUS PERIODICALLY
========================================================= */

setInterval(() => {

    checkAPIStatus();

}, 30000);
let allUnits = [];

async function loadUnits() {

    const container = document.getElementById("units");

    if (!container) return;

    container.innerHTML =
        `<div class="loading">Loading units...</div>`;

    try {

        allUnits = await apiRequest("/units");

        renderUnits();

    } catch (error) {

        container.innerHTML =
            `<div class="error">${esc(error.message)}</div>`;
    }
}


function renderUnits() {

    const container = document.getElementById("units");

    if (!container) return;

    const searchBox = document.getElementById("unitSearch");

    const search = searchBox
        ? searchBox.value.toLowerCase().trim()
        : "";

    const filtered = allUnits.filter(unit =>
        String(unit.unitName || "")
            .toLowerCase()
            .includes(search)
    );

    if (filtered.length === 0) {

        container.innerHTML =
            `<div class="empty">No units available.</div>`;

        return;
    }

    let html = `
        <div class="panel">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Unit Name</th>
                    </tr>
                </thead>

                <tbody>
    `;

    filtered.forEach(unit => {

        html += `
            <tr>
                <td><b>#${unit.id}</b></td>
                <td>${esc(unit.unitName)}</td>
            </tr>
        `;
    });

    html += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = html;
}


function filterUnits() {
    renderUnits();
}