import React from 'react';
import './App.css';
import FlashSale from './components/FlashSale/FlashSale';
import ProductCarousel from './components/ProductCarousel/ProductCarousel';
import ProductCategories from './components/ProductCategories/ProductCategories';

// Sample product data
const flashSaleProducts = [
  {
    image: 'https://via.placeholder.com/250x300?text=7+Thoi+Quen+Hieu+Qua',
    title: '7 Thói Quen Hiệu Quả - The 7 Habits Of Highly Effective Peopl...',
    author: 'Stephen R. Covey',
    discountedPrice: 150000,
    originalPrice: 250000,
    discountPercent: 40,
    soldCount: 7,
    totalStock: 10
  },
  {
    image: 'https://via.placeholder.com/250x300?text=Storytelling',
    title: 'Storytelling - Lay Động Lòng Người Bằng Chuyện Kể - Khổ Lớn',
    author: '',
    discountedPrice: 167000,
    originalPrice: 246000,
    discountPercent: 32,
    soldCount: 4,
    totalStock: 10
  },
  {
    image: 'https://via.placeholder.com/250x300?text=Tri+Tue+Do+Thai',
    title: 'Trí Tuệ Do Thái (Tái Bản 2022)',
    author: 'Eran Katz',
    discountedPrice: 132000,
    originalPrice: 189000,
    discountPercent: 30,
    soldCount: 5,
    totalStock: 10
  },
  {
    image: 'https://via.placeholder.com/250x300?text=Don+Tam+Ly',
    title: 'Những Đòn Tâm Lý Trong Thuyết Phục (Tái Bản 2023)',
    author: 'Robert B. Cialdini',
    discountedPrice: 160000,
    originalPrice: 229000,
    discountPercent: 30,
    soldCount: 3,
    totalStock: 10
  },
  {
    image: 'https://via.placeholder.com/250x300?text=Hoc+Cach+Hoc',
    title: 'Học Cách Học (Tái Bản)',
    author: '',
    discountedPrice: 111000,
    originalPrice: 159000,
    discountPercent: 30,
    soldCount: 6,
    totalStock: 10
  }
];

function App() {
  return (
    <div className="App">
      <div className="container">
        <FlashSale />
        <ProductCarousel products={flashSaleProducts} />
        <ProductCategories />
      </div>
      
      {/* Floating action buttons */}
      <div className="floating-buttons">
        <button className="floating-button floating-button-like">
          👍
        </button>
        <button className="floating-button floating-button-chat">
          💬
        </button>
      </div>
    </div>
  );
}

export default App;



