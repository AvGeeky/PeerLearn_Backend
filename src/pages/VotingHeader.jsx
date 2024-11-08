import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { UserPlus, BookOpen, LogIn, Phone, Mail } from 'lucide-react';

function ActionButton({ icon: Icon, children, to }) {
    const navigate = useNavigate();

    return (
        <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate(to)}
            className="group flex items-center gap-3 bg-gradient-to-r from-sky-500 to-sky-600 hover:from-sky-600 hover:to-sky-700
        px-8 py-4 rounded-xl text-xl font-semibold text-white shadow-lg hover:shadow-xl transition-all duration-300"
        >
            <Icon className="w-6 h-6" />
            {children}
        </motion.button>
    );
}

function ContactItem({ icon: Icon, title, details }) {
    return (
        <motion.div
            whileHover={{ scale: 1.05 }}
            className="flex items-center gap-4 bg-blue-600/30 p-4 rounded-xl backdrop-blur-sm"
        >
            <div className="bg-sky-500 p-3 rounded-full">
                <Icon className="w-6 h-6 text-white" />
            </div>
            <div>
                <h3 className="text-lg font-semibold text-sky-300">{title}</h3>
                {details.map((detail, index) => (
                    <p key={index} className="text-white/90">{detail}</p>
                ))}
            </div>
        </motion.div>
    );
}

export function VotingHeader() {
    const navigate = useNavigate();

    return (
        <div className="min-h-screen w-full bg-gradient-to-br from-blue-900 to-blue-700 overflow-hidden">
            <div className="max-w-[2500px] mx-auto px-36 py-38 flex flex-col min-h-screen">
                <nav className="flex justify-between items-center mb-8">
                    <motion.h1
                        initial={{ opacity: 0, x: -20 }}
                        animate={{ opacity: 1, x: 0 }}
                        className="text-4xl font-bold"
                    >
                        <span className="text-white">SecUr</span>
                        <span className="text-sky-400">Vote</span>
                    </motion.h1>

                    <div className="flex gap-8">
                        {[
                            { name: 'About', path: '/about' },
                            { name: 'Features', path: '/features' },
                            { name: 'Steps', path: '/voting-steps' }
                        ].map((item) => (
                            <motion.button
                                key={item.name}
                                whileHover={{ scale: 1.1 }}
                                onClick={() => navigate(item.path)}
                                className="text-xl text-sky-200 hover:text-white transition-colors duration-300 px-4 py-2 rounded-lg hover:bg-sky-800/50"
                            >
                                {item.name}
                            </motion.button>
                        ))}
                    </div>
                </nav>

                <div className="flex-1 grid lg:grid-cols-2 gap-12 items-center py-12">
                    <motion.div
                        initial={{ opacity: 0, scale: 0.8 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ type: "spring", duration: 0.8 }}
                        className="order-2 lg:order-1 flex justify-center"
                    >
                        <motion.img
                            src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-OPDZwYi3GzZWa0bpTh8kJFWyMj7CdM.png"
                            alt="Voter placing ballot in voting box"
                            className="w-full max-w-md mx-auto drop-shadow-2xl"
                            initial={{ y: 20, opacity: 0 }}
                            animate={{ y: 0, opacity: 1 }}
                            transition={{
                                type: "spring",
                                stiffness: 260,
                                damping: 20,
                                duration: 1.5,
                                repeat: Infinity,
                                repeatType: "reverse"
                            }}
                        />
                    </motion.div>

                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ delay: 0.4 }}
                        className="order-1 lg:order-2 text-center lg:text-left"
                    >
                        <h2 className="text-5xl lg:text-6xl font-bold text-white mb-6">
                            Be a part of the{' '}
                            <span className="text-transparent bg-clip-text bg-gradient-to-r from-sky-400 to-sky-200">
                decision
              </span>
                        </h2>

                        <motion.h3
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ delay: 0.6 }}
                            className="text-6xl lg:text-7xl font-bold mb-12 text-transparent bg-clip-text bg-gradient-to-r from-sky-300 via-sky-200 to-white"
                        >
                            Vote Today
                        </motion.h3>

                        <div className="flex flex-wrap gap-6 justify-center lg:justify-start">
                            <ActionButton to="/registration" icon={UserPlus}>
                                Register
                            </ActionButton>
                            <ActionButton to="/voting-instructionss" icon={BookOpen}>
                                Instructions
                            </ActionButton>
                            <ActionButton to="/login" icon={LogIn}>
                                Login
                            </ActionButton>
                        </div>
                    </motion.div>
                </div>

                <div className="mt-auto grid md:grid-cols-2 gap-6 max-w-4xl mx-auto">
                    <ContactItem
                        icon={Phone}
                        title="Contact Us"
                        details={["1800 9090 32", "1800 9000 64"]}
                    />
                    <ContactItem
                        icon={Mail}
                        title="Email Us"
                        details={[
                            "complaint@electionindia.gov.in",
                            "info@electionindia.gov.in"
                        ]}
                    />
                </div>

                <motion.footer
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ delay: 0.8 }}
                    className="text-center py-8 mt-8"
                >
                    <motion.p
                        whileHover={{ scale: 1.1 }}
                        className="text-2xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-sky-400 to-sky-200 mb-2"
                    >
                        SSN COLLEGE OF ENGINEERING, KALAVAKKAM
                    </motion.p>
                    <motion.p
                        whileHover={{ scale: 1.1 }}
                        className="text-lg text-sky-300"
                    >
                        Made with ❣️ by Saipranav M, Rahul V S and Pragadish
                    </motion.p>
                </motion.footer>
            </div>
        </div>
    );
}