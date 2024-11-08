import * as React from "react";
import {Link} from "react-router-dom";

export function AboutPage() {
    return (
        <main
            className="min-h-screen w-screen overflow-x-hidden bg-gradient-to-b from-blue-800 to-green-400 text-white"
            role="main"
            aria-label="About SafeVote"
        >
            <div className="w-full max-w-[1400px] mx-auto px-4 lg:px-8 py-16 lg:py-24">
                <section className="flex flex-col lg:flex-row gap-8 lg:gap-16">
                    {/* Title Section */}
                    <div className="lg:w-1/4">
                        <h1 className="text-5xl lg:text-6xl font-bold text-white mb-8 lg:mb-0 lg:sticky lg:top-24">
                            About SafeVote
                        </h1>
                    </div>

                    {/* Content Section */}
                    <div className="lg:w-3/4 space-y-8">
                        {/* Introduction */}
                        <div className="prose prose-lg prose-invert">
                            <p className="text-xl leading-relaxed">
                                <span className="font-semibold text-sky-300">SafeVote</span> is an advanced, secure voting platform engineered to uphold
                                the integrity and confidentiality of digital voting. Our system represents the future of democratic participation,
                                combining cutting-edge technology with user-friendly design.
                            </p>
                        </div>

                        {/* Key Features Grid */}
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-12">
                            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-6">
                                <h2 className="text-xl font-semibold text-sky-300 mb-3">Blockchain Security</h2>
                                <p className="text-gray-100">
                                    Utilizing advanced blockchain technology to ensure each vote is immutable,
                                    traceable, and protected from tampering while maintaining complete anonymity.
                                </p>
                            </div>

                            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-6">
                                <h2 className="text-xl font-semibold text-sky-300 mb-3">Multi-Factor Authentication</h2>
                                <p className="text-gray-100">
                                    Robust identity verification system using biometric data, OTP verification,
                                    and government ID validation to prevent fraudulent voting.
                                </p>
                            </div>

                            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-6">
                                <h2 className="text-xl font-semibold text-sky-300 mb-3">Real-Time Analytics</h2>
                                <p className="text-gray-100">
                                    Advanced analytics dashboard providing real-time insights into voting patterns
                                    and turnout while maintaining voter privacy.
                                </p>
                            </div>

                            <div className="bg-white/10 backdrop-blur-sm rounded-xl p-6">
                                <h2 className="text-xl font-semibold text-sky-300 mb-3">Accessibility</h2>
                                <p className="text-gray-100">
                                    Designed for all users with screen reader support, keyboard navigation,
                                    and multiple language options to ensure inclusive participation.
                                </p>
                            </div>
                        </div>

                        {/* Mission Statement */}
                        <div className="bg-gradient-to-r from-blue-600/20 to-green-500/20 rounded-xl p-8 mt-12">
                            <h2 className="text-2xl font-bold text-white mb-4">Our Mission</h2>
                            <p className="text-lg text-gray-100">
                                To revolutionize the democratic process by providing a secure, transparent, and
                                accessible digital voting platform that empowers citizens to participate in
                                elections with complete confidence in the integrity of their vote.
                            </p>
                        </div>

                        {/* Technical Specifications */}
                        <div className="mt-12">
                            <h2 className="text-2xl font-bold text-white mb-6">Technical Excellence</h2>
                            <ul className="space-y-4 text-gray-100">
                                <li className="flex items-start">
                                    <span className="text-sky-300 mr-2">•</span>
                                    End-to-end encryption ensuring vote privacy and security
                                </li>
                                <li className="flex items-start">
                                    <span className="text-sky-300 mr-2">•</span>
                                    Distributed ledger technology preventing single points of failure
                                </li>
                                <li className="flex items-start">
                                    <span className="text-sky-300 mr-2">•</span>
                                    Smart contract implementation for automated and transparent vote counting
                                </li>
                                <li className="flex items-start">
                                    <span className="text-sky-300 mr-2">•</span>
                                    Regular security audits and penetration testing
                                </li>
                            </ul>
                        </div>
                    </div>
                </section>

                {/* Back to Home Button */}
                <div className="flex justify-center mt-16">
                    <Link
                        to="/"
                        className="inline-block px-8 py-3 text-lg font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                    >
                        BACK TO HOME
                    </Link>
                </div>
            </div>
        </main>
    );
}