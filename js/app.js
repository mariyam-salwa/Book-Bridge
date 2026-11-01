/**
 * ==============================================================================
 * BOOK BRIDGE - Main Client Application Logic (Member 2 Implementation)
 * File: js/app.js
 * Handles:
 *  - Live search & multi-criteria filtering for catalog (explore.html)
 *  - Modal popups for Book Handover Requests (material-detail.html)
 *  - Interactive Dashboard actions (Accept/Decline handover, status updates)
 *  - Toast notification banners for campus user actions
 * ==============================================================================
 */

document.addEventListener('DOMContentLoaded', function () {
  initLiveCatalogFiltering();
  initHandoverModal();
  initDashboardActions();
  initToastNotificationContainer();
});

// ------------------------------------------------------------------------------
// 1. LIVE SEARCH & FILTERING (explore.html)
// ------------------------------------------------------------------------------
function initLiveCatalogFiltering() {
  const searchInput = document.getElementById('filterSearchInput');
  const deptSelect = document.getElementById('deptSelect');
  const typeCheckboxes = document.querySelectorAll('input[name="type"]');
  const priceRadios = document.querySelectorAll('input[name="price"]');
  const clearBtn = document.getElementById('clearFiltersBtn');
  const cards = document.querySelectorAll('.material-card');
  const resultsCounter = document.querySelector('section > div span strong');

  if (!cards.length) return;

  function filterCards() {
    const keyword = searchInput ? searchInput.value.toLowerCase().trim() : '';
    const selectedDept = deptSelect ? deptSelect.value.toLowerCase() : 'all';

    // Active types: "digital", "physical"
    const allowedTypes = Array.from(typeCheckboxes)
      .filter(cb => cb.checked)
      .map(cb => cb.value.toLowerCase());

    // Selected price: "all", "free", "paid"
    let selectedPrice = 'all';
    priceRadios.forEach(radio => {
      if (radio.checked) selectedPrice = radio.value;
    });

    let visibleCount = 0;

    cards.forEach(card => {
      const title = card.querySelector('.card-title')?.textContent.toLowerCase() || '';
      const desc = card.querySelector('.card-desc')?.textContent.toLowerCase() || '';
      const meta = card.querySelector('.card-meta')?.textContent.toLowerCase() || '';
      const priceText = card.querySelector('.card-price')?.textContent.toLowerCase() || '';

      // Type Check
      const isPhysical = card.querySelector('.badge-physical') !== null || priceText.includes('cash handover');
      const isDigital = card.querySelector('.badge-digital') !== null || priceText.includes('free') || card.querySelector('.badge-free') !== null;
      
      let typeMatches = false;
      if (isPhysical && allowedTypes.includes('physical')) typeMatches = true;
      if (isDigital && allowedTypes.includes('digital')) typeMatches = true;
      if (!isPhysical && !isDigital && allowedTypes.length > 0) typeMatches = true;

      // Keyword Check
      const textMatches = !keyword || title.includes(keyword) || desc.includes(keyword) || meta.includes(keyword);

      // Department Check
      let deptMatches = true;
      if (selectedDept !== 'all') {
        deptMatches = meta.includes(selectedDept);
      }

      // Price Check
      let priceMatches = true;
      const isFree = priceText.includes('₹0') || priceText.includes('free');
      if (selectedPrice === 'free' && !isFree) priceMatches = false;
      if (selectedPrice === 'paid' && isFree) priceMatches = false;

      // Combine conditions
      if (typeMatches && textMatches && deptMatches && priceMatches) {
        card.style.display = 'flex';
        visibleCount++;
      } else {
        card.style.display = 'none';
      }
    });

    // Update Counter
    if (resultsCounter) {
      resultsCounter.textContent = `${visibleCount} item${visibleCount === 1 ? '' : 's'}`;
    }
  }

  // Attach Event Listeners
  if (searchInput) searchInput.addEventListener('input', filterCards);
  if (deptSelect) deptSelect.addEventListener('change', filterCards);
  typeCheckboxes.forEach(cb => cb.addEventListener('change', filterCards));
  priceRadios.forEach(r => r.addEventListener('change', filterCards));

  // Reset Filters
  if (clearBtn) {
    clearBtn.addEventListener('click', function () {
      if (searchInput) searchInput.value = '';
      if (deptSelect) deptSelect.value = 'all';
      typeCheckboxes.forEach(cb => cb.checked = true);
      const allPriceRadio = document.querySelector('input[name="price"][value="all"]');
      if (allPriceRadio) allPriceRadio.checked = true;
      filterCards();
    });
  }

  // Quick URL parameter check (e.g., explore.html?type=physical)
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get('type') === 'physical') {
    typeCheckboxes.forEach(cb => {
      cb.checked = (cb.value === 'physical');
    });
    filterCards();
  }
}

// ------------------------------------------------------------------------------
// 2. MODAL FOR BOOK HANDOVER REQUEST (material-detail.html)
// ------------------------------------------------------------------------------
function initHandoverModal() {
  const modal = document.getElementById('purchaseModal');
  if (!modal) return;

  // Open modal buttons
  document.querySelectorAll('[data-open-modal="purchaseModal"]').forEach(btn => {
    btn.addEventListener('click', () => modal.classList.add('open'));
  });

  // Close buttons
  modal.querySelectorAll('.modal-close, .btn-outline').forEach(btn => {
    btn.addEventListener('click', () => modal.classList.remove('open'));
  });

  // Close when clicking overlay backdrop
  modal.addEventListener('click', (e) => {
    if (e.target === modal) modal.classList.remove('open');
  });

  // Handle Handover Request Submission
  const handoverForm = modal.querySelector('form');
  if (handoverForm) {
    handoverForm.addEventListener('submit', function (e) {
      e.preventDefault();
      const buyerName = document.getElementById('buyerName')?.value || 'Student';
      const locationSelect = document.getElementById('preferredLocation');
      const locationText = locationSelect ? locationSelect.options[locationSelect.selectedIndex].text : 'Campus';

      modal.classList.remove('open');
      showToast(`Purchase request sent to seller! Handover at: ${locationText}.`, 'success');
    });
  }
}

// ------------------------------------------------------------------------------
// 3. DASHBOARD ACTION BUTTONS & STATUS UPDATES (dashboard.html)
// ------------------------------------------------------------------------------
function initDashboardActions() {
  // Accept / Complete Handover button handlers in table
  document.querySelectorAll('.table-custom tbody tr').forEach(row => {
    const actionCell = row.querySelector('td:last-child');
    const statusBadge = row.querySelector('.badge');
    if (!actionCell || !statusBadge) return;

    // Check for "Accept" or "Action" buttons
    const acceptBtn = actionCell.querySelector('.btn-primary, .btn-secondary');
    if (acceptBtn && acceptBtn.textContent.includes('Accept')) {
      acceptBtn.addEventListener('click', function (e) {
        e.preventDefault();
        statusBadge.className = 'badge badge-success';
        statusBadge.textContent = 'Accepted (Meetup Scheduled)';
        actionCell.innerHTML = `<button class="btn btn-sm btn-outline" onclick="completeHandover(this)">Mark Handover Done</button>`;
        showToast('Request accepted! Coordinate meetup with buyer.', 'success');
      });
    }
  });

  // Saved Items removal
  document.querySelectorAll('#tab-saved .material-card button').forEach(btn => {
    btn.addEventListener('click', function () {
      const card = this.closest('.material-card');
      if (card) {
        card.style.opacity = '0';
        setTimeout(() => {
          card.remove();
          showToast('Resource removed from your saved library.', 'info');
        }, 300);
      }
    });
  });
}

// Global helper for Mark Handover Done
window.completeHandover = function (btnElement) {
  const row = btnElement.closest('tr');
  if (row) {
    const statusBadge = row.querySelector('.badge');
    if (statusBadge) {
      statusBadge.className = 'badge badge-completed';
      statusBadge.style.backgroundColor = '#10b981';
      statusBadge.style.color = '#ffffff';
      statusBadge.textContent = 'Completed (Cash Paid)';
    }
    btnElement.parentElement.innerHTML = `<span style="font-size:0.8rem; color:#10b981; font-weight:600;">✓ Transaction Closed</span>`;
    showToast('Transaction completed! Cash payment confirmed.', 'success');
  }
};

// ------------------------------------------------------------------------------
// 4. TOAST NOTIFICATION COMPONENT
// ------------------------------------------------------------------------------
function initToastNotificationContainer() {
  if (document.getElementById('bb-toast-container')) return;

  const container = document.createElement('div');
  container.id = 'bb-toast-container';
  container.style.cssText = `
    position: fixed;
    top: 24px;
    right: 24px;
    z-index: 9999;
    display: flex;
    flex-direction: column;
    gap: 10px;
    pointer-events: none;
  `;
  document.body.appendChild(container);
}

window.showToast = function (message, type = 'info') {
  initToastNotificationContainer();
  const container = document.getElementById('bb-toast-container');

  const toast = document.createElement('div');
  toast.style.cssText = `
    min-width: 280px;
    max-width: 380px;
    padding: 12px 18px;
    border-radius: 8px;
    font-size: 0.9rem;
    font-weight: 500;
    color: #ffffff;
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
    opacity: 0;
    transform: translateY(-10px);
    transition: all 0.3s ease;
    pointer-events: auto;
    display: flex;
    align-items: center;
    gap: 10px;
  `;

  // Color theme by type
  if (type === 'success') {
    toast.style.backgroundColor = '#10b981';
    toast.innerHTML = `<span>✓</span><span>${message}</span>`;
  } else if (type === 'danger' || type === 'error') {
    toast.style.backgroundColor = '#ef4444';
    toast.innerHTML = `<span>⚠️</span><span>${message}</span>`;
  } else {
    toast.style.backgroundColor = '#2563eb';
    toast.innerHTML = `<span>ℹ️</span><span>${message}</span>`;
  }

  container.appendChild(toast);

  // Animate in
  setTimeout(() => {
    toast.style.opacity = '1';
    toast.style.transform = 'translateY(0)';
  }, 10);

  // Auto remove after 3.5s
  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(-10px)';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
};
