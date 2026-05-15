import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { jwtDecode } from 'jwt-decode';

export const useUserStore = create(
  persist(
    (set) => ({
      user: null, // Lưu thông tin user sau khi decode (username, roles...)
      isAuthenticated: false, // Trạng thái đã đăng nhập hay chưa

      // Hàm này gọi sau khi Login thành công
      setLoginSuccess: (token) => {
        try {
          const decoded = jwtDecode(token); // Giải mã token từ backend [cite: 17]
          set({
            user: {
              username: decoded.sub, // Lấy username từ trường 'sub' trong token của bạn
              ...decoded
            },
            isAuthenticated: true,
          });
        } catch (error) {
          console.error("Token không hợp lệ:", error);
        }
      },

      // Hàm này gọi khi Logout hoặc bị kick-out (lỗi 1006) [cite: 20, 25]
      clearUser: () => {
        set({ user: null, isAuthenticated: false });
      },
    }),
    {
      name: 'user-storage', // Tên key lưu trong LocalStorage để khi F5 trang không bị mất login
    }
  )
);