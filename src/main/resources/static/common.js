const API_BASE = '/api';
const token = localStorage.getItem('jwt');

function parseJwt(t) {
    try {
        return JSON.parse(decodeURIComponent(atob(t.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')));
    } catch (e) { return null; }
}

const userPayload = token ? parseJwt(token) : null;
const userRoles = userPayload ? userPayload.roles.map(r => r.replace('ROLE_', '')) : [];

function checkAuth() {
    if (!token || !userPayload) {
        localStorage.removeItem('jwt');
        window.location.href = 'login.html';
    }
}

async function apiFetch(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = { method, headers };

    if (body) {
        config.body = JSON.stringify(body);
    }

    const res = await fetch(`${API_BASE}${endpoint}`, config);

    if (res.status === 401 || res.status === 403) {

        if (endpoint !== '/auth/login') {
            localStorage.removeItem('jwt');
            window.location.href = 'login.html';
        }

        const err = await res.json().catch(() => ({}));

        throw new Error(err.message || 'Неправильний логiн або пароль');
    }

    if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        throw new Error(err.message || `Помилка ${res.status}`);
    }

    if (res.status === 204 || res.headers.get('content-length') === '0') {
        return null;
    }

    return await res.json();
}

function renderNav() {
    const nav = document.getElementById('main-nav');
    if (!nav) return;

    let html = `<button onclick="location.href='profile.html'">Профіль</button>`;
    html += `<button onclick="location.href='assignments.html'">Доручення</button>`;

    if (userRoles.includes('MANAGER') || userRoles.includes('ADMIN')) {
        html += `<button onclick="location.href='assignment-create.html'">+ Створити доручення</button>`;
        html += `<button onclick="location.href='employees.html'">Співробітники</button>`;
    }
    if (userRoles.includes('ADMIN')) {
        html += `<button onclick="location.href='catalogs.html'">Довідники</button>`;
    }
    html += `<button onclick="localStorage.removeItem('jwt'); location.href='login.html'">Вийти</button>`;
    nav.innerHTML = html;
}