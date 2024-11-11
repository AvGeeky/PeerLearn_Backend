import * as React from "react";
import { useNavigate } from "react-router-dom";

function InputField({ label, type = "text", value, onChange }) {
    return (
        <label className="flex flex-col text-lg text-white mb-6">
            <span className="mb-2 font-medium">{label}</span>
            <input
                type={type}
                value={value}
                onChange={onChange}
                className="flex shrink-0 self-stretch w-full bg-white/20 text-white h-[43px] px-4 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                aria-label={label}
                required
            />
        </label>
    );
}

function LoginForm() {
    const [formData, setFormData] = React.useState({
        email: "",
        password: "",
    });
    const [error, setError] = React.useState("");
    const navigate = useNavigate();

    const handleChange = (field) => (event) => {
        setFormData((prev) => ({
            ...prev,
            [field]: event.target.value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError("");
        try {
            const response = await login(formData);
            console.log("Login successful", response);
            // Store the token in localStorage
            localStorage.setItem('token', response.token);
            navigate("/personal-info");
        } catch (err) {
            setError(err.message || "An error occurred during login");
        }
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col w-full max-w-md">
            <h1 className="text-4xl font-bold text-white mb-8">Login</h1>

            <div className="bg-white/10 backdrop-blur-md rounded-xl p-8 space-y-4">
                <InputField
                    label="Email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange("email")}
                />

                <InputField
                    label="Password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange("password")}
                />

                {error && <p className="text-red-500 text-sm">{error}</p>}

                <button
                    type="submit"
                    className="w-full px-6 py-3 mt-6 text-lg font-semibold text-blue-900 bg-white rounded-lg hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 transition-all"
                >
                    LOGIN
                </button>

                <p className="mt-4 text-center text-white">
                    Not a user?{" "}
                    <a
                        href="/registration"
                        className="text-blue-200 hover:text-blue-100 focus:outline-none focus:ring-2 focus:ring-blue-500 rounded"
                    >
                        Register now
                    </a>
                </p>
            </div>
        </form>
    );
}

function LoginIllustration() {
    return (
        <div className="w-full md:w-1/2 flex items-center justify-center p-8 mb-8 md:mb-0">
            <img
                loading="lazy"
                src="https://cdn.builder.io/api/v1/image/assets/TEMP/6221094fe2c7c8ad2a39e4d14fce6f10acddd7c0a340bec06c10c8831a8c6af3"
                alt="Login page illustration"
                className="max-w-full h-auto object-contain"
            />
        </div>
    );
}

export function LoginPage() {
    return (
        <main className="min-h-screen w-screen flex items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden">
            <div className="w-full px-4 py-8 flex flex-col md:flex-row items-center justify-between">
                <LoginIllustration />
                <div className="w-full md:w-1/2 flex justify-center">
                    <LoginForm />
                </div>
            </div>
        </main>
    );
}
