<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Weekly Progress</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <style>
        #weeklyProgressChart {
            max-width: 600px;  
            max-height: 400px; 
        }
    </style>
</head>
<body>
<div class="container">
    <%
        HttpSession sess = request.getSession(false);
        String id = (String) sess.getAttribute("uid");
        if (id == null) {
            response.sendRedirect("associateHome.jsp");
        }
    %>
    
    <h1>Select a Week to View Work Hours</h1>
    <form id="weeklyForm" onsubmit="fetchWeeklyProgress(event)">
        <input type="hidden" id="id" name="id" value="<%= id %>">
        <input type="week" id="week" name="week" required>
        <input type="submit" value="Show Weekly Progress">
    </form>
    <canvas id="weeklyProgressChart"></canvas>

    <script>
        function fetchWeeklyProgress(event) {
            event.preventDefault();

            var id = document.getElementById('id').value;
            var week = document.getElementById('week').value;

            fetch('WeeklyProgressServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    'id': id,
                    'week': week
                })
            })
            .then(response => response.json())
            .then(data => {
                var ctx = document.getElementById('weeklyProgressChart').getContext('2d');
                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: data.labels,
                        datasets: [{
                            label: 'Work Hours',
                            data: data.values,
                            backgroundColor: '#36A2EB', 
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: {
                                    display: true,
                                    text: 'Hours'
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Date'
                                }
                            }
                        },
                        plugins: {
                            legend: {
                                display: true,
                            },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        let label = context.label || '';
                                        if (label) {
                                            label += ': ';
                                        }
                                        if (context.parsed !== null) {
                                            label += context.parsed + ' hours';
                                        }
                                        return label;
                                    }
                                }
                            }
                        }
                    }
                });
            })
        }
    </script>
    <form action="assosiateHome.jsp" method="post">
        <input type="submit" value="Back" class="back-button">
    </form>
</div>
</body>
</html>
