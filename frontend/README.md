# BookStore Frontend - React Application

Giao diện React.js cho cửa hàng sách với Flash Sale và carousel sản phẩm.

## Tính năng

- **Flash Sale Banner**: Banner với countdown timer tự động
- **Product Carousel**: Carousel ngang hiển thị sách đang giảm giá
- **Product Cards**: Thẻ sản phẩm với hình ảnh, giá, phần trăm giảm giá, và thanh tiến trình bán hàng
- **Product Categories**: Danh mục sản phẩm
- **Responsive Design**: Giao diện tương thích với mobile và desktop

## Cài đặt

```bash
cd frontend
npm install
```

## Chạy ứng dụng

```bash
npm start
```

Ứng dụng sẽ chạy tại [http://localhost:3000](http://localhost:3000)

## Build cho production

```bash
npm run build
```

## Cấu trúc thư mục

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── FlashSale/
│   │   │   ├── FlashSale.js
│   │   │   └── FlashSale.css
│   │   ├── ProductCarousel/
│   │   │   ├── ProductCarousel.js
│   │   │   └── ProductCarousel.css
│   │   ├── ProductCard/
│   │   │   ├── ProductCard.js
│   │   │   └── ProductCard.css
│   │   └── ProductCategories/
│   │       ├── ProductCategories.js
│   │       └── ProductCategories.css
│   ├── App.js
│   ├── App.css
│   ├── index.js
│   └── index.css
├── package.json
└── README.md
```

## Components

### FlashSale
Component hiển thị banner Flash Sale với countdown timer tự động đếm ngược.

### ProductCarousel
Component carousel ngang để hiển thị danh sách sản phẩm, có thể scroll bằng nút mũi tên.

### ProductCard
Component thẻ sản phẩm hiển thị:
- Hình ảnh sách
- Tiêu đề và tác giả
- Giá gốc và giá giảm
- Badge phần trăm giảm giá
- Thanh tiến trình số lượng đã bán

### ProductCategories
Component hiển thị danh mục sản phẩm dạng grid.



