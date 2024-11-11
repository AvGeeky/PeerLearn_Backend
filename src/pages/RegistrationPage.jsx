import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Eye, EyeOff } from 'lucide-react';
import { motion } from "framer-motion";

const RegistrationPage = () => {
    const [formData, setFormData] = useState({
        name: "",
        secretCode: "",
        email: "",
        password: "",
        reEnterPassword: ""
    });

    const [showPasswordFields, setShowPasswordFields] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [showReEnterPassword, setShowReEnterPassword] = useState(false);
    const navigate = useNavigate();

    const validateInitialFields = () => {
        if (formData.name && formData.secretCode === "1234" && formData.email.includes("@")) {
            setShowPasswordFields(true);
            setErrorMessage("");
        } else {
            setErrorMessage("Please enter the correct Name, Secret Code, and Email.");
        }
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (formData.password !== formData.reEnterPassword) {
            setErrorMessage("Passwords do not match.");
            return;
        }
        navigate("/login");
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const togglePasswordVisibility = (field) => {
        if (field === 'password') {
            setShowPassword(!showPassword);
        } else {
            setShowReEnterPassword(!showReEnterPassword);
        }
    };

    const inputVariants = {
        focus: { scale: 1.02, transition: { duration: 0.3 } }
    };

    return (
        <main className="min-h-screen w-screen flex items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden p-4 md:p-8">
            <motion.div
                className="w-full max-w-7xl flex flex-col md:flex-row items-center justify-between bg-black/30 backdrop-blur-md rounded-3xl p-8 md:p-12 shadow-2xl"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
            >
                <motion.div
                    className="w-full md:w-1/2 flex justify-center mb-12 md:mb-0"
                    initial={{ x: -100, opacity: 0 }}
                    animate={{ x: 0, opacity: 1 }}
                    transition={{ duration: 0.8, type: "spring" }}
                >
                    <motion.img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/67446702eabcd31d9bbf137ef3170bebac9ba0f224c2ee2157d3a073fec81b9f"
                        alt="Student registration illustration"
                        className="max-w-full h-auto object-contain rounded-2xl shadow-xl"
                        whileHover={{ scale: 1.05 }}
                        transition={{ duration: 0.3 }}
                    />
                </motion.div>

                <div className="w-full md:w-1/2 flex justify-center">
                    <form onSubmit={handleSubmit} className="w-full max-w-xl space-y-8">
                        <motion.h1
                            className="text-5xl font-bold text-white mb-12 text-center"
                            initial={{ y: -20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{ duration: 0.5, delay: 0.2 }}
                        >
                            Student Registration
                        </motion.h1>

                        <motion.div
                            className="space-y-8"
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            transition={{ duration: 0.5, delay: 0.4 }}
                        >
                            <motion.div className="w-full" variants={inputVariants}>
                                <label className="block text-white text-lg font-medium mb-3" htmlFor="name">Name</label>
                                <motion.input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleInputChange}
                                    className="w-full px-5 py-4 rounded-xl focus:ring-4 focus:ring-blue-500 focus:outline-none bg-white/20 text-white text-lg placeholder-gray-300"
                                    required
                                    placeholder="Enter your full name"
                                    whileFocus="focus"
                                />
                            </motion.div>

                            <motion.div className="w-full" variants={inputVariants}>
                                <label className="block text-white text-lg font-medium mb-3" htmlFor="secretCode">Secret Code</label>
                                <motion.input
                                    type="text"
                                    id="secretCode"
                                    name="secretCode"
                                    value={formData.secretCode}
                                    onChange={handleInputChange}
                                    className="w-full px-5 py-4 rounded-xl focus:ring-4 focus:ring-blue-500 focus:outline-none bg-white/20 text-white text-lg placeholder-gray-300"
                                    required
                                    placeholder="Enter the secret code"
                                    whileFocus="focus"
                                />
                            </motion.div>

                            <motion.div className="w-full" variants={inputVariants}>
                                <label className="block text-white text-lg font-medium mb-3" htmlFor="email">Email</label>
                                <motion.input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleInputChange}
                                    className="w-full px-5 py-4 rounded-xl focus:ring-4 focus:ring-blue-500 focus:outline-none bg-white/20 text-white text-lg placeholder-gray-300"
                                    required
                                    placeholder="your.email@example.com"
                                    whileFocus="focus"
                                />
                            </motion.div>

                            {errorMessage && (
                                <motion.p
                                    className="text-red-300 mt-4 text-lg font-medium"
                                    initial={{ opacity: 0 }}
                                    animate={{ opacity: 1 }}
                                    transition={{ duration: 0.3 }}
                                >
                                    {errorMessage}
                                </motion.p>
                            )}

                            {!showPasswordFields && (
                                <motion.button
                                    type="button"
                                    onClick={validateInitialFields}
                                    className="w-full px-8 py-4 mt-8 text-xl font-semibold text-white bg-blue-600 rounded-xl hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 focus:outline-none transition-all duration-300 transform hover:scale-105"
                                    whileHover={{ scale: 1.05 }}
                                    whileTap={{ scale: 0.95 }}
                                >
                                    Verify Details
                                </motion.button>
                            )}

                            {showPasswordFields && (
                                <motion.div
                                    className="space-y-8"
                                    initial={{ opacity: 0, y: 20 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    transition={{ duration: 0.5 }}
                                >
                                    <h2 className="text-3xl font-bold text-white mt-12 mb-8 text-center">Create Your Password</h2>
                                    <motion.div variants={inputVariants}>
                                        <label className="block text-white text-lg font-medium mb-3" htmlFor="password">Password</label>
                                        <div className="relative">
                                            <motion.input
                                                type={showPassword ? "text" : "password"}
                                                id="password"
                                                name="password"
                                                value={formData.password}
                                                onChange={handleInputChange}
                                                className="w-full px-5 py-4 rounded-xl focus:ring-4 focus:ring-blue-500 focus:outline-none bg-white/20 text-white text-lg placeholder-gray-300"
                                                required
                                                placeholder="Enter your password"
                                                whileFocus="focus"
                                            />
                                            <button
                                                type="button"
                                                onClick={() => togglePasswordVisibility('password')}
                                                className="absolute inset-y-0 right-0 pr-5 flex items-center text-white"
                                            >
                                                {showPassword ? <EyeOff className="h-6 w-6" /> : <Eye className="h-6 w-6" />}
                                            </button>
                                        </div>
                                    </motion.div>
                                    <motion.div variants={inputVariants}>
                                        <label className="block text-white text-lg font-medium mb-3" htmlFor="reEnterPassword">Re-enter Password</label>
                                        <div className="relative">
                                            <motion.input
                                                type={showReEnterPassword ? "text" : "password"}
                                                id="reEnterPassword"
                                                name="reEnterPassword"
                                                value={formData.reEnterPassword}
                                                onChange={handleInputChange}
                                                className="w-full px-5 py-4 rounded-xl focus:ring-4 focus:ring-blue-500 focus:outline-none bg-white/20 text-white text-lg placeholder-gray-300"
                                                required
                                                placeholder="Re-enter your password"
                                                whileFocus="focus"
                                            />
                                            <button
                                                type="button"
                                                onClick={() => togglePasswordVisibility('reEnterPassword')}
                                                className="absolute inset-y-0 right-0 pr-5 flex items-center text-white"
                                            >
                                                {showReEnterPassword ? <EyeOff className="h-6 w-6" /> : <Eye className="h-6 w-6" />}
                                            </button>
                                        </div>
                                    </motion.div>
                                    <motion.button
                                        type="submit"
                                        className="w-full px-8 py-4 mt-8 text-xl font-semibold text-white bg-blue-600 rounded-xl hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 focus:outline-none transition-all duration-300 transform hover:scale-105"
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                    >
                                        SUBMIT
                                    </motion.button>
                                </motion.div>
                            )}
                        </motion.div>
                    </form>
                </div>
            </motion.div>
        </main>
    );
};

export default RegistrationPage;