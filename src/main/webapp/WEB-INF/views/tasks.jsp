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
    </style>
</head>
<body>

<h1>Task Manager API</h1>

<div class="row">
    <input id="name" placeholder="Task name"/>
    <input id="desc" placeholder="Description"/>

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

<ul id="tasks"></ul>

<script>
    const BASE = "http://localhost:8080";

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

    async function loadTasks() {
        const res = await fetch(BASE + "/api/tasks");
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
                status: document.getElementById("status").value,
                priority: document.getElementById("priority").value
            })
        });

        // optional: clear inputs after add
        document.getElementById("name").value = "";
        document.getElementById("desc").value = "";
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

    loadTasks();
</script>

<h3>In making of this project, I used:</h3>
<p>Java, Springboot for REST operations, JPA for database operations, Implemented CRUD operations, DTOs, MVC structure</p>

</body>
</html>
