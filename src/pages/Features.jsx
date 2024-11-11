import React from 'react';
import { Link } from 'react-router-dom';
import { Shield, Database, Hash, Zap, Lock, Clock } from 'lucide-react';

const features = [
    {
        icon: Shield,
        title: "Secured by RSA/HMAC encryption",
        description: "Military-grade encryption ensuring vote integrity"
    },
    {
        icon: Database,
        title: "Blockchain-based Vote Storage",
        description: "Immutable and transparent vote recording"
    },
    {
        icon: Hash,
        title: "Verifiable votes using Hash-IDs",
        description: "Each vote is uniquely traceable and verifiable"
    },
    {
        icon: Zap,
        title: "Easy to use interface",
        description: "Intuitive design for seamless voting experience"
    },
    {
        icon: Lock,
        title: "Multiple authentication levels",
        description: "Multi-factor authentication for enhanced security"
    },
    {
        icon: Clock,
        title: "Faster voting process",
        description: "Efficient and quick voting system"
    }
];

function FeatureCard({ Icon, title, description }) {
    return (
        <div className="bg-white/5 backdrop-blur-sm rounded-xl p-6 hover:bg-white/10 transition-all duration-200 flex items-start gap-4">
            <Icon className="w-8 h-8 text-blue-400 flex-shrink-0" />
            <div>
                <h3 className="text-xl font-medium text-white mb-2">{title}</h3>
                <p className="text-blue-200">{description}</p>
            </div>
        </div>
    );
}

export function Features() {
    return (
        <main className="min-h-screen  w-full bg-gradient-to-br from-blue-950 via-blue-800 to-blue-600 px-4 py-16">
            <div className="w-full max-w-[1700px] mx-auto px-14 lg:px-18 py-25 lg:py-30">
                <header className="text-center mb-16">
                    <h1 className="text-5xl font-bold text-white mb-4">
                        Our Features
                    </h1>
                    <p className="text-xl text-blue-200">
                        Advanced technology ensuring secure and efficient voting
                    </p>
                </header>

                <section className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    {features.map((feature, index) => (
                        <FeatureCard
                            key={index}
                            Icon={feature.icon}
                            title={feature.title}
                            description={feature.description}
                        />
                    ))}
                </section>

                <footer className="mt-16 text-center">
                    <Link
                        to="/"
                        className="inline-block px-8 py-3 text-lg font-semibold text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                    >
                        BACK TO HOME
                    </Link>
                </footer>
            </div>
        </main>
    );
}