import React, { useState, useEffect } from 'react';
import './FlashSale.css';

const FlashSale = () => {
  const [timeLeft, setTimeLeft] = useState({
    hours: 1,
    minutes: 2,
    seconds: 33
  });

  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft(prev => {
        let { hours, minutes, seconds } = prev;
        
        if (seconds > 0) {
          seconds--;
        } else if (minutes > 0) {
          minutes--;
          seconds = 59;
        } else if (hours > 0) {
          hours--;
          minutes = 59;
          seconds = 59;
        } else {
          clearInterval(timer);
        }
        
        return { hours, minutes, seconds };
      });
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  const formatTime = (value) => {
    return value.toString().padStart(2, '0');
  };

  return (
    <div className="flash-sale-banner">
      <div className="flash-sale-content">
        <div className="flash-sale-title">
          <span className="lightning-icon">⚡</span>
          <span className="flash-sale-text">FLASH SALE</span>
        </div>
        <div className="countdown-section">
          <span className="countdown-label">Kết thúc trong</span>
          <div className="countdown-timer">
            <div className="time-box">{formatTime(timeLeft.hours)}</div>
            <span className="time-separator">:</span>
            <div className="time-box">{formatTime(timeLeft.minutes)}</div>
            <span className="time-separator">:</span>
            <div className="time-box">{formatTime(timeLeft.seconds)}</div>
          </div>
        </div>
        <a href="#all" className="view-all-link">
          Xem tất cả
          <span className="arrow-icon">→</span>
        </a>
      </div>
    </div>
  );
};

export default FlashSale;



