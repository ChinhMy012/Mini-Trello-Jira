import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import {
  User,
  Lock,
  Mail,
  ArrowRight,
  Kanban,
  Eye,
  EyeOff,
} from "lucide-react";
import { authApi } from "../../api/authApi";
import { useUserStore } from "../../store/useUserStore";

const AuthPage = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const setLoginSuccess = useUserStore((state) => state.setLoginSuccess);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        // Đăng nhập: Chỉ gửi username và password [cite: 15, 16]
        const res = await authApi.login({
          username: formData.username,
          password: formData.password,
        });

        if (res.code === 1000) {
          setLoginSuccess(res.data.token); // Giải mã và lưu info user [cite: 17]
          alert("Chào mừng trở lại!");
        }
      } else {
        // Đăng ký: Gửi đầy đủ thông tin [cite: 13]
        const res = await authApi.register(formData);
        if (res.code === 1000) {
          alert("Đăng ký thành công! Mời bạn đăng nhập.");
          setIsLogin(true);
        }
      }
    } catch (error) {
      // Hiển thị message lỗi từ Backend (ví dụ: User đã tồn tại) [cite: 4, 6]
      alert(error.response?.data?.message || "Đã có lỗi xảy ra");
    }
  };
  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-[#f8fafc] p-4">
      {/* Card Container - Bo góc và đổ bóng */}
      <div className="relative w-full max-w-[1000px] h-[650px] bg-white rounded-[2.5rem] shadow-[0_20px_60px_rgba(0,0,0,0.1)] flex overflow-hidden border border-slate-100">
        {/* BÊN TRÁI: Vùng hình ảnh & Brand */}
        <div className="relative hidden md:flex w-[45%] h-full bg-slate-900 p-12 flex-col justify-between overflow-hidden">
          <div className="absolute inset-0 opacity-40">
            <img
              src="https://images.unsplash.com/photo-1611224885990-ab7363d1f2a9?q=80&w=1973&auto=format&fit=crop"
              className="w-full h-full object-cover"
              alt="bg"
            />
          </div>
          <div className="absolute inset-0 bg-gradient-to-b from-blue-600/30 to-slate-900/90" />

          <div className="relative z-10">
            <div className="flex items-center gap-2 text-white mb-8">
              <Kanban size={32} className="text-blue-400" />
              <span className="text-2xl font-bold tracking-tight">
                Blueprint.
              </span>
            </div>
            <h2 className="text-4xl font-bold text-white leading-tight">
              Quản trị dự án <br />
              <span className="text-blue-400">theo cách tinh gọn.</span>
            </h2>
          </div>
          <div className="relative z-10 text-slate-400 text-xs tracking-widest uppercase font-semibold">
            © 2026 Blueprint System
          </div>
        </div>

        {/* BÊN PHẢI: Vùng Form nhập liệu */}
        <div className="flex-1 h-full bg-white flex flex-col justify-center px-8 lg:px-16">
          <div className="w-full max-w-sm mx-auto">
            <div className="mb-10 text-center md:text-left">
              <h3 className="text-3xl font-bold text-slate-800 mb-2">
                {isLogin ? "Đăng nhập" : "Tạo tài khoản"}
              </h3>
              <p className="text-slate-500 font-medium">
                Vui lòng điền thông tin của bạn
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
              {/* Username Input */}
              <div className="space-y-1.5">
                <label className="text-xs font-bold text-slate-500 uppercase ml-1">
                  Username
                </label>
                <div className="relative group">
                  <User
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-blue-500 transition-colors"
                    size={18}
                  />
                  <input
                    type="text"
                    placeholder="Tên đăng nhập..."
                    className="w-full pl-11 pr-4 py-3.5 bg-slate-50 border border-slate-200 rounded-2xl focus:bg-white focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all"
                    onChange={(e) =>
                      setFormData({ ...formData, username: e.target.value })
                    }
                    required
                  />
                </div>
              </div>

              {/* Email Input - Chỉ hiện khi Đăng ký */}
              <AnimatePresence>
                {!isLogin && (
                  <motion.div
                    initial={{ opacity: 0, height: 0 }}
                    animate={{ opacity: 1, height: "auto" }}
                    exit={{ opacity: 0, height: 0 }}
                    className="space-y-1.5 overflow-hidden"
                  >
                    <label className="text-xs font-bold text-slate-500 uppercase ml-1">
                      Email
                    </label>
                    <div className="relative">
                      <Mail
                        className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
                        size={18}
                      />
                      <input
                        type="email"
                        placeholder="example@gmail.com"
                        className="w-full pl-11 pr-4 py-3.5 bg-slate-50 border border-slate-200 rounded-2xl focus:bg-white focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all"
                        onChange={(e) =>
                          setFormData({ ...formData, email: e.target.value })
                        }
                        required
                      />
                    </div>
                  </motion.div>
                )}
              </AnimatePresence>

              {/* Password Input */}
              <div className="space-y-1.5">
                <label className="text-xs font-bold text-slate-500 uppercase ml-1">
                  Mật khẩu
                </label>
                <div className="relative group">
                  <Lock
                    className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-blue-500 transition-colors"
                    size={18}
                  />

                  {/* SỬA TYPE Ở ĐÂY */}
                  <input
                    type={showPassword ? "text" : "password"}
                    placeholder="••••••••"
                    className="w-full pl-11 pr-12 py-3.5 bg-slate-50 border border-slate-200 rounded-2xl focus:bg-white focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all"
                    onChange={(e) =>
                      setFormData({ ...formData, password: e.target.value })
                    }
                    required
                  />

                  {/* THÊM NÚT BẤM NÀY */}
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-blue-500 transition-colors p-1"
                  >
                    {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                  </button>
                </div>
              </div>

              <button className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 rounded-2xl flex items-center justify-center gap-2 transition-all active:scale-[0.98] shadow-lg shadow-blue-100">
                {isLogin ? "Đăng Nhập" : "Tạo Tài Khoản"}
                <ArrowRight size={20} />
              </button>
            </form>

            <div className="mt-8 text-center text-sm">
              <span className="text-slate-500 font-medium">
                {isLogin ? "Chưa có tài khoản?" : "Đã có tài khoản?"}
              </span>
              <button
                onClick={() => setIsLogin(!isLogin)}
                className="ml-2 text-blue-600 hover:text-blue-700 font-bold"
              >
                {isLogin ? "Đăng ký ngay" : "Đăng nhập"}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthPage;
