/**
 * ==============================================================================
 * BOOK BRIDGE - Form Validation Module (Member 2 Implementation)
 * File: js/validation.js
 * Handles:
 *  - Student Registration validation (Email, Password match, Phone, Student ID)
 *  - Authentication & Role Login validation
 *  - Material Upload validation (Free price toggle, File size, Book price)
 * ==============================================================================
 */

document.addEventListener('DOMContentLoaded', function () {
  initRegistrationValidation();
  initLoginValidation();
  initUploadPricingLogic();
});

// ------------------------------------------------------------------------------
// 1. REGISTRATION FORM VALIDATION
// ------------------------------------------------------------------------------
function initRegistrationValidation() {
  const regForm = document.querySelector('form[action="dashboard.html"]');
  if (!regForm || !document.getElementById('regFullName')) return;

  const fullNameInput = document.getElementById('regFullName');
  const studentIdInput = document.getElementById('regStudentId');
  const emailInput = document.getElementById('regEmail');
  const phoneInput = document.getElementById('regPhone');
  const passInput = document.getElementById('regPass');
  const confirmPassInput = document.getElementById('regConfirmPass');

  regForm.addEventListener('submit', function (e) {
    // 1. Name Check
    if (fullNameInput.value.trim().length < 3) {
      e.preventDefault();
      alertValidation('Please enter your full legal name (minimum 3 characters).', fullNameInput);
      return;
    }

    // 2. Student ID Check
    if (studentIdInput.value.trim().length < 4) {
      e.preventDefault();
      alertValidation('Please enter a valid Student ID Number (e.g., 2024CS041).', studentIdInput);
      return;
    }

    // 3. Email Check
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(emailInput.value.trim())) {
      e.preventDefault();
      alertValidation('Please provide a valid college or personal email address.', emailInput);
      return;
    }

    // 4. Phone Check (10 digits)
    const phoneClean = phoneInput.value.replace(/[^0-9]/g, '');
    if (phoneClean.length < 10) {
      e.preventDefault();
      alertValidation('Please enter a valid 10-digit mobile number for campus handover coordination.', phoneInput);
      return;
    }

    // 5. Password Length Check
    if (passInput.value.length < 6) {
      e.preventDefault();
      alertValidation('Password must be at least 6 characters long.', passInput);
      return;
    }

    // 6. Confirm Password Match
    if (passInput.value !== confirmPassInput.value) {
      e.preventDefault();
      alertValidation('Passwords do not match! Please re-type your confirm password.', confirmPassInput);
      return;
    }

    // Success notification
    if (window.showToast) {
      window.showToast('Registration successful! Welcome to BookBridge.', 'success');
    }
  });
}

// ------------------------------------------------------------------------------
// 2. LOGIN FORM VALIDATION
// ------------------------------------------------------------------------------
function initLoginValidation() {
  const loginForm = document.getElementById('loginForm');
  if (!loginForm) return;

  const emailInput = document.getElementById('loginEmail');
  const passwordInput = document.getElementById('loginPassword');

  loginForm.addEventListener('submit', function (e) {
    if (!emailInput.value.trim()) {
      e.preventDefault();
      alertValidation('Please enter your college email or Student ID.', emailInput);
      return;
    }

    if (passwordInput.value.trim().length === 0) {
      e.preventDefault();
      alertValidation('Please enter your account password.', passwordInput);
      return;
    }
  });
}

// ------------------------------------------------------------------------------
// 3. UPLOAD PRICING & PAYMENT TYPE INTERACTION
// ------------------------------------------------------------------------------
function initUploadPricingLogic() {
  const digitalPriceInput = document.getElementById('digitalPrice');
  const digitalPaymentSelect = document.getElementById('digitalPaymentType');
  const fileInput = document.getElementById('digitalFileInput');

  if (digitalPriceInput && digitalPaymentSelect) {
    // When payment type dropdown changes
    digitalPaymentSelect.addEventListener('change', function () {
      if (this.value.toLowerCase() === 'free') {
        digitalPriceInput.value = 0;
        digitalPriceInput.setAttribute('disabled', 'true');
      } else {
        digitalPriceInput.removeAttribute('disabled');
        if (parseFloat(digitalPriceInput.value) <= 0) {
          digitalPriceInput.value = 50; // default initial paid price
        }
      }
    });

    // When price input changes
    digitalPriceInput.addEventListener('input', function () {
      const price = parseFloat(this.value);
      if (price <= 0 || isNaN(price)) {
        digitalPaymentSelect.value = 'Free';
      } else {
        digitalPaymentSelect.value = 'Paid';
      }
    });
  }

  // Validate File Size on Dropzone selection (25 MB max)
  if (fileInput) {
    fileInput.addEventListener('change', function () {
      if (this.files && this.files[0]) {
        const file = this.files[0];
        const fileSizeMB = file.size / (1024 * 1024);
        
        if (fileSizeMB > 25) {
          alert('File size exceeds the 25MB limit. Please upload a compressed PDF or smaller file.');
          this.value = ''; // Reset input
        } else {
          const dropZoneText = document.querySelector('.drop-zone div:nth-child(2)');
          if (dropZoneText) {
            dropZoneText.textContent = `Selected: ${file.name} (${fileSizeMB.toFixed(1)} MB)`;
            dropZoneText.style.color = 'var(--primary)';
          }
        }
      }
    });
  }
}

// Helper: Visual feedback & focus
function alertValidation(message, elementToFocus) {
  alert(message);
  if (elementToFocus) {
    elementToFocus.focus();
    elementToFocus.style.borderColor = 'var(--danger)';
    setTimeout(() => {
      elementToFocus.style.borderColor = '';
    }, 2500);
  }
}
