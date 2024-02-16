document.addEventListener("DOMContentLoaded", function() {
    var data = {
        labels: ['Доходы', 'Расходы'],
        datasets: [{
            label: 'Доходы и расходы',
            data: [3000, 1500], // Пример данных (замените их на свои)
            backgroundColor: [
                'rgba(75, 192, 192, 0.2)',
                'rgba(255, 99, 132, 0.2)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255, 99, 132, 1)'
            ],
            borderWidth: 1
        }]
    };

    // Настройки диаграммы
    var options = {
        responsive: false, // Отключаем респонсивность диаграммы
        maintainAspectRatio: false, // Отключаем поддержку соотношения сторон
        scales: {
            y: {
                beginAtZero: true // Начинать ось Y с нуля
            }
        }
    };

    // Получаем элемент canvas
    var ctx = document.getElementById('chart').getContext('2d');

    // Создаем экземпляр диаграммы
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
    });
});
