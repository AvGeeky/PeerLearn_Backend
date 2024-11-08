import * as React from "react";
import { useNavigate } from "react-router-dom";

const stepsData = [
    {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/c552bce18a369b1620211ac2a43be44185172214c398f8ff6322dd85c9f125d2?placeholderIfAbsent=true&apiKey=856530a2bc8b4fad805f6d030062538d",
        imageAlt: "Registration step icon",
        description: "Register yourself by filling your university issued secret code and E-Mail. Set your username and password"
    },
    {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/5a525bf2f4d94246249b8bcd8ba73fd4888b480bae394d6b8c451d4fe4cba5c2?placeholderIfAbsent=true&apiKey=856530a2bc8b4fad805f6d030062538d",
        imageAlt: "Sign in step icon",
        description: "Sign in as user, and verify your personal information. Note down your HASH-ID"
    },
    {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/0481a2e0298cab5b235a94f174484297664f7d872e86e26198d3640c24fd741a?placeholderIfAbsent=true&apiKey=856530a2bc8b4fad805f6d030062538d",
        imageAlt: "Vote option step icon",
        description: "Go to vote option on dashboard"
    },
    {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/bc8d18d7914cfb3d0778699d9ac4f86302a2bd34896a59cfed54a459ffa9e41f?placeholderIfAbsent=true&apiKey=856530a2bc8b4fad805f6d030062538d",
        imageAlt: "Cast vote step icon",
        description: "Cast your vote!"
    },
    {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/f89a6a7e345cb0ad3735dcd6f9eecdca5025b607b486aa61864d38f573e80bab?placeholderIfAbsent=true&apiKey=856530a2bc8b4fad805f6d030062538d",
        imageAlt: "Verify vote step icon",
        description: "Verify your vote by checking if the HASH-ID has been marked as voted in the public verification page"
    }
];

function StepItem({ imageUrl, imageAlt, description, index }) {
    return (
        <div className="flex items-center bg-white/10 backdrop-blur-sm rounded-lg p-6 transition-all duration-200 hover:bg-white/20">
            <div className="flex-shrink-0 mr-6">
                <div className="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
                    {index + 1}
                </div>
            </div>
            <img
                loading="lazy"
                src={imageUrl}
                alt={imageAlt}
                className="object-contain w-16 h-16 mr-6"
            />
            <p className="flex-1 text-lg text-white">
                {description}
            </p>
        </div>
    );
}

export function VotingSteps() {
    const navigate = useNavigate();

    const handleBackToHome = () => {
        navigate("/");
    };

    return (
        <main className="min-h-screen w-screen flex items-center justify-center bg-gradient-to-b from-[#FF4E6E] via-[#DA5F9C] to-[#2E33D1] overflow-hidden px-4 py-8">
            <div className="w-full max-w-4xl bg-white/10 backdrop-blur-md rounded-xl p-8 shadow-2xl">
                <h1 className="text-4xl md:text-5xl font-bold text-white text-center mb-10">
                    Follow these easy steps
                </h1>

                <div className="space-y-6">
                    {stepsData.map((step, index) => (
                        <StepItem
                            key={index}
                            imageUrl={step.imageUrl}
                            imageAlt={step.imageAlt}
                            description={step.description}
                            index={index}
                        />
                    ))}
                </div>

                <nav className="flex justify-center mt-10">
                    <button
                        onClick={handleBackToHome}
                        className="px-6 py-3 text-lg font-semibold text-blue-900 bg-white rounded-full hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 transition-all"
                        aria-label="Return to home page"
                    >
                        BACK TO HOME
                    </button>
                </nav>
            </div>
        </main>
    );
}

export default VotingSteps;