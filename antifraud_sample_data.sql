-- Bảng News
CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    image_url VARCHAR(255),
    author VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    status VARCHAR(20)
);
INSERT INTO news (title, content, image_url, author, created_at, updated_at, status) VALUES
('Cảnh báo lừa đảo qua điện thoại', 'Nội dung chi tiết về các chiêu trò lừa đảo qua điện thoại...', 'https://img.example.com/news1.jpg', 'Admin', NOW(), NOW(), 'active'),
('Lừa đảo chuyển khoản ngân hàng', 'Cách nhận biết và phòng tránh lừa đảo chuyển khoản...', 'https://img.example.com/news2.jpg', 'Admin', NOW(), NOW(), 'active');

-- Bảng Video
CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    video_url VARCHAR(255),
    thumbnail_url VARCHAR(255),
    created_at TIMESTAMP,
    status VARCHAR(20)
);
INSERT INTO video (title, description, video_url, thumbnail_url, created_at, status) VALUES
('Phòng chống lừa đảo online', 'Video hướng dẫn các bước phòng chống lừa đảo trên mạng.', 'https://youtube.com/watch?v=abc123', 'https://img.example.com/video1.jpg', NOW(), 'active');

-- Bảng Report
CREATE TABLE report (
    id SERIAL PRIMARY KEY,
    reporter_name VARCHAR(100),
    reporter_email VARCHAR(100),
    phone VARCHAR(20),
    description TEXT,
    evidence_url VARCHAR(255),
    created_at TIMESTAMP,
    status VARCHAR(20),
    admin_note TEXT
);
INSERT INTO report (reporter_name, reporter_email, phone, description, evidence_url, created_at, status, admin_note) VALUES
('Nguyen Van A', 'a@gmail.com', '0912345678', 'Tôi nhận được cuộc gọi nghi ngờ lừa đảo...', 'https://img.example.com/evidence1.jpg', NOW(), 'pending', '');

-- Bảng Product
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price NUMERIC(12,2),
    image_url VARCHAR(255),
    stock INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    status VARCHAR(20)
);
INSERT INTO product (name, description, price, image_url, stock, created_at, updated_at, status) VALUES
('Khóa chống trộm điện tử', 'Khóa cửa thông minh giúp phòng chống trộm cắp, lừa đảo.', 1200000, 'https://img.example.com/product1.jpg', 10, NOW(), NOW(), 'available'),
('Camera an ninh mini', 'Camera nhỏ gọn, dễ lắp đặt, hỗ trợ giám sát phòng chống lừa đảo.', 850000, 'https://img.example.com/product2.jpg', 20, NOW(), NOW(), 'available'); 