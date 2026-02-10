<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Task Manager</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 900px; margin: 40px auto; }
        input, select, button { padding: 8px; margin: 4px; }
        li { margin: 10px 0; padding: 10px; border: 1px solid #ddd; border-radius: 8px; }
        .meta { font-size: 12px; color: #666; margin-top: 6px; }
        .row { display: flex; flex-wrap: wrap; gap: 6px; align-items: center; }
        .label { font-weight: bold; margin-right: 6px; }
        hr { margin: 16px 0; }
    </style>
</head>
<body>

<h1>Task Manager API</h1>

<!-- ADD TASK -->
<div class="row">
    <input id="name" placeholder="Task name"/>
    <input id="desc" placeholder="Description"/>
    <input id="dueDate" type="date"/>

    <span class="label">Status:</span>
    <select id="status">
        <option value="TODO" selected>TODO</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="DONE">DONE</option>
    </select>

    <span class="label">Priority:</span>
    <select id="priority">
        <option value="LOW">LOW</option>
        <option value="MEDIUM" selected>MEDIUM</option>
        <option value="HIGH">HIGH</option>
    </select>

    <button onclick="addTask()">Add</button>
</div>

<hr/>

<!-- FILTERS -->
<h3>Filters</h3>
<div class="row">
    <span class="label">Q:</span>
    <input id="f-q" placeholder="Search in name/description"/>

    <span class="label">Status:</span>
    <select id="f-status">
        <option value="">ANY</option>
        <option value="TODO">TODO</option>
        <option value="IN_PROGRESS">IN_PROGRESS</option>
        <option value="DONE">DONE</option>
    </select>

    <span class="label">Priority:</span>
    <select id="f-priority">
        <option value="">ANY</option>
        <option value="LOW">LOW</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="HIGH">HIGH</option>
    </select>

    <span class="label">From:</span>
    <input id="f-from" type="date"/>

    <span class="label">To:</span>
    <input id="f-to" type="date"/>

    <button onclick="applyFilters()">Apply</button>
    <button onclick="clearFilters()">Clear</button>
</div>

<ul id="tasks"></ul>

<script>
    // Use same-origin so it works on any port/host (no hardcoded localhost)
    const BASE = "";

    function esc(s) {
        return String(s ?? "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }

    function fmtDate(d) {
        if (!d) return "";
        var dt = new Date(d);
        return isNaN(dt.getTime()) ? d : dt.toLocaleString();
    }

    function statusOptions(selected) {
        var s = selected || "TODO";
        var html = "";
        html += '<option value="TODO"' + (s == "TODO" ? " selected" : "") + '>TODO</option>';
        html += '<option value="IN_PROGRESS"' + (s == "IN_PROGRESS" ? " selected" : "") + '>IN_PROGRESS</option>';
        html += '<option value="DONE"' + (s == "DONE" ? " selected" : "") + '>DONE</option>';
        return html;
    }

    function priorityOptions(selected) {
        var p = selected || "MEDIUM";
        var html = "";
        html += '<option value="LOW"' + (p == "LOW" ? " selected" : "") + '>LOW</option>';
        html += '<option value="MEDIUM"' + (p == "MEDIUM" ? " selected" : "") + '>MEDIUM</option>';
        html += '<option value="HIGH"' + (p == "HIGH" ? " selected" : "") + '>HIGH</option>';
        return html;
    }

    function initFiltersFromUrl() {
        const p = new URLSearchParams(window.location.search);

        document.getElementById("f-q").value = p.get("q") || "";
        document.getElementById("f-status").value = p.get("status") || "";
        document.getElementById("f-priority").value = p.get("priority") || "";
        document.getElementById("f-from").value = p.get("dueDateFrom") || "";
        document.getElementById("f-to").value = p.get("dueDateTo") || "";
    }

    function applyFilters() {
        const p = new URLSearchParams();

        const q = document.getElementById("f-q").value.trim();
        const status = document.getElementById("f-status").value;
        const priority = document.getElementById("f-priority").value;
        const from = document.getElementById("f-from").value;
        const to = document.getElementById("f-to").value;

        if (q) p.set("q", q);
        if (status) p.set("status", status);
        if (priority) p.set("priority", priority);
        if (from) p.set("dueDateFrom", from);
        if (to) p.set("dueDateTo", to);

        history.replaceState(null, "", window.location.pathname + (p.toString() ? "?" + p.toString() : ""));
        loadTasks();
    }

    function clearFilters() {
        history.replaceState(null, "", window.location.pathname);
        initFiltersFromUrl();
        loadTasks();
    }

    async function loadTasks() {
        // Forward the page query string to the API
        const qs = window.location.search || "";
        const res = await fetch(BASE + "/api/tasks" + qs);
        const tasks = await res.json();

        const ul = document.getElementById("tasks");
        ul.innerHTML = "";

        tasks.forEach(function (t) {
            const li = document.createElement("li");

            li.innerHTML =
                '<div class="row">' +
                '<b>#' + esc(t.id) + '</b>' +

                '<input value="' + esc(t.name) + '" id="name-' + t.id + '" placeholder="Task name">' +
                '<input value="' + esc(t.description) + '" id="desc-' + t.id + '" placeholder="Description">' +
                '<input type="date" value="' + esc(t.dueDate || "") + '" id="dueDate-' + t.id + '">' +

                '<span class="label">Status:</span>' +
                '<select id="status-' + t.id + '">' +
                statusOptions(t.status) +
                '</select>' +

                '<span class="label">Priority:</span>' +
                '<select id="priority-' + t.id + '">' +
                priorityOptions(t.priority) +
                '</select>' +

                '<button onclick="updateTask(' + t.id + ')">Update</button>' +
                '<button onclick="deleteTask(' + t.id + ')">Delete</button>' +
                '</div>' +

                '<div class="meta">' +
                'Created: ' + esc(fmtDate(t.createdAt)) +
                ' | Updated: ' + esc(fmtDate(t.updatedAt)) +
                '</div>';

            ul.appendChild(li);
        });
    }

    async function addTask() {
        await fetch(BASE + "/api/tasks", {
            method: "POST",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify({
                name: document.getElementById("name").value,
                description: document.getElementById("desc").value,
                dueDate: document.getElementById("dueDate").value,
                status: document.getElementById("status").value,
                priority: document.getElementById("priority").value
            })
        });

        // optional: clear inputs after add
        document.getElementById("name").value = "";
        document.getElementById("desc").value = "";
        document.getElementById("dueDate").value = "";
        document.getElementById("status").value = "TODO";
        document.getElementById("priority").value = "MEDIUM";

        loadTasks();
    }

    async function updateTask(id) {
        await fetch(BASE + "/api/tasks/" + id, {
            method: "PUT",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify({
                name: document.getElementById("name-"+id).value,
                description: document.getElementById("desc-"+id).value,
                dueDate: document.getElementById("dueDate-"+id).value,
                status: document.getElementById("status-"+id).value,
                priority: document.getElementById("priority-"+id).value
            })
        });
        loadTasks();
    }

    async function deleteTask(id) {
        await fetch(BASE + "/api/tasks/" + id, { method: "DELETE" });
        loadTasks();
    }

    // init
    initFiltersFromUrl();
    loadTasks();
</script>

<h3>In making of this project, I used:</h3>
<p>Java, Springboot for REST operations, JPA for database operations, Implemented CRUD operations, DTOs, MVC structure</p>

</body>
</html>
