// Không tự động điền token từ URL nữa

function generateOTP() {
  return Math.floor(100000 + Math.random() * 900000).toString();
}

document.addEventListener('DOMContentLoaded', function() {
  const tokenInput = document.getElementById('token');
  const step1 = document.getElementById('step1');
  const step2 = document.getElementById('step2');
  const messageDiv = document.getElementById('message');
  const verifyBtn = document.getElementById('verifyTokenBtn');

  verifyBtn.addEventListener('click', async function() {
    const token = tokenInput.value;
    messageDiv.textContent = '';
    try {
      const res = await fetch('/req/verify-reset-token', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token })
      });
      const text = await res.text();
      if (res.ok) {
        step1.style.display = 'none';
        step2.style.display = 'block';
        messageDiv.style.color = 'green';
        messageDiv.textContent = 'OTP hợp lệ, hãy nhập mật khẩu mới!';
      } else {
        messageDiv.style.color = 'red';
        messageDiv.textContent = text;
      }
    } catch (err) {
      messageDiv.style.color = 'red';
      messageDiv.textContent = 'Lỗi xác thực OTP!';
    }
  });

  document.getElementById('resetPasswordForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    const token = tokenInput.value;
    const newPassword = document.getElementById('newPassword').value;
    messageDiv.textContent = '';
    try {
      const res = await fetch('/req/reset-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token, newPassword })
      });
      const text = await res.text();
      if (res.ok) {
        messageDiv.style.color = 'green';
        messageDiv.textContent = text + ' Đang chuyển về trang đăng nhập...';
        setTimeout(() => {
          window.location.href = '/req/login';
        }, 2000);
      } else {
        messageDiv.style.color = 'red';
        messageDiv.textContent = text;
      }
    } catch (err) {
      messageDiv.style.color = 'red';
      messageDiv.textContent = 'Lỗi gửi yêu cầu!';
    }
  });
}); 