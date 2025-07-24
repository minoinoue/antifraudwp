document.getElementById('forgotPasswordForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const messageDiv = document.getElementById('message');
  messageDiv.textContent = '';
  try {
    const res = await fetch('/req/forgot-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email })
    });
    const text = await res.text();
    if (res.ok) {
      messageDiv.style.color = 'green';
      messageDiv.textContent = text;
      setTimeout(() => {
        window.location.href = '/req/reset-password-page';
      }, 1000);
    } else {
      messageDiv.style.color = 'red';
      messageDiv.textContent = text;
    }
  } catch (err) {
    messageDiv.style.color = 'red';
    messageDiv.textContent = 'Lỗi gửi yêu cầu!';
  }
}); 