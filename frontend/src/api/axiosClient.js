import axios from "axios";

// Khởi tạo cấu hình mặc định
const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // 
  withCredentials: true, // Bắt buộc phải có để gửi/nhận Cookie 
  headers: {
    "Content-Type": "application/json", // 
  },
});

// Thêm Interceptor cho Response để xử lý dữ liệu trả về và bắt lỗi chung
axiosClient.interceptors.response.use(
  (response) => {
    // API của bạn luôn trả về cấu trúc { code, message, data }
    // Ta return thẳng response.data để các file gọi API dùng cho tiện
    return response.data;
  },
  (error) => {
    const responseData = error.response?.data;

    // Bắt chính xác lỗi 1006: Phiên đăng nhập bị hủy/hết hạn [cite: 20]
    if (responseData && responseData.code === 1006) {
      console.warn("Phiên đăng nhập hết hạn hoặc bị kick-out!");
      
      // Xóa thông tin user đang lưu ở LocalStorage (ta sẽ cấu hình sau)
      localStorage.removeItem("user-storage"); 
      
      // Chuyển hướng người dùng về trang login [cite: 21]
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default axiosClient;