document.addEventListener('DOMContentLoaded', () => {
    const portfolioSection = document.getElementById('portfolio');
    const newProject = document.createElement('p');
    newProject.textContent = 'Проект на Python: Анализ данных';
    portfolioSection.appendChild(newProject);
});
