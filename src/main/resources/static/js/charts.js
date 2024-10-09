function createFlowCoverageChart(flowCoverage) {
    const ctx = document.getElementById('flowCoverageChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(flowCoverage),
            datasets: [{
                label: 'Number of Tests',
                data: Object.values(flowCoverage),
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Number of Tests'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Flow'
                    }
                }
            }
        }
    });
}

function createServiceCoverageChart(serviceCoverage) {
    const ctx = document.getElementById('serviceCoverageChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: Object.keys(serviceCoverage),
            datasets: [{
                data: Object.values(serviceCoverage),
                backgroundColor: [
                    'rgba(255, 99, 132, 0.6)',
                    'rgba(54, 162, 235, 0.6)',
                    'rgba(255, 206, 86, 0.6)',
                    'rgba(75, 192, 192, 0.6)',
                    'rgba(153, 102, 255, 0.6)',
                ],
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Service Method Coverage'
                }
            }
        }
    });
}