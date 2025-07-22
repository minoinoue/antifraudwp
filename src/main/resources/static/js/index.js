// Fetch News
fetch('/api/news')
  .then(res => res.json())
  .then(newsList => {
    const newsContainer = document.getElementById('news-list');
    if (newsContainer) {
      newsContainer.innerHTML = newsList.map(news => `
        <div class="news-card">
          <img src="${news.imageUrl}" alt="news"/>
          <div class="news-content">
            <h3>${news.title}</h3>
            <p>${news.content.substring(0, 120)}...</p>
            <span class="news-meta">${news.author} | ${news.createdAt ? news.createdAt.substring(0,10) : ''}</span>
          </div>
        </div>
      `).join('');
    }
  });

// Fetch Videos
fetch('/api/videos')
  .then(res => res.json())
  .then(videoList => {
    const videoContainer = document.getElementById('video-list');
    if (videoContainer) {
      videoContainer.innerHTML = videoList.map(video => `
        <div class="video-card">
          <img src="${video.thumbnailUrl}" alt="video"/>
          <div class="video-content">
            <h3>${video.title}</h3>
            <p>${video.description.substring(0, 80)}...</p>
            <a href="${video.videoUrl}" target="_blank">Xem video</a>
          </div>
        </div>
      `).join('');
    }
  });

// Fetch Products
fetch('/api/products')
  .then(res => res.json())
  .then(productList => {
    const productContainer = document.getElementById('product-list');
    if (productContainer) {
      productContainer.innerHTML = productList.map(product => `
        <div class="product-card">
          <img src="${product.imageUrl}" alt="product"/>
          <div class="product-content">
            <h3>${product.name}</h3>
            <p>${product.description.substring(0, 80)}...</p>
            <span class="product-price">${product.price} VNĐ</span>
            <button>Mua ngay</button>
          </div>
        </div>
      `).join('');
    }
  });

// Submit Report
const reportForm = document.getElementById('report-form');
if (reportForm) {
  reportForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    const data = {
      reporterName: this.reporterName.value,
      reporterEmail: this.reporterEmail.value,
      phone: this.phone.value,
      description: this.description.value,
      evidenceUrl: this.evidenceUrl.value
    };
    const res = await fetch('/api/reports', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });
    const msg = document.getElementById('report-message');
    if (res.ok) {
      msg.textContent = 'Gửi báo cáo thành công!';
      msg.style.color = 'green';
      this.reset();
    } else {
      msg.textContent = 'Gửi báo cáo thất bại!';
      msg.style.color = 'red';
    }
  });
} 