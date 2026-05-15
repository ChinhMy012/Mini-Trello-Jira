import { Routes, Route, Navigate } from 'react-router-dom';
import Login from '../pages/Auth/Login';

const AppRoutes = () => {
  return (
    <Routes>
      {/* Mặc định vào web sẽ đá sang trang login */}
      <Route path="/" element={<Navigate to="/login" />} />
      
      {/* Trang Login */}
      <Route path="/login" element={<Login />} />

      {/* Sau này bạn thêm các trang Dashboard, Board ở đây */}
      <Route path="*" element={<div>Trang này không tồn tại - 404</div>} />
    </Routes>
  );
};

export default AppRoutes;