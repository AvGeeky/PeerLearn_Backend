from google.adk import Runner
from google.adk.sessions import InMemorySessionService
from google.adk.agents import Agent
from google.adk import Runner
import requests,os
from dotenv import load_dotenv
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), ".env"))

def youtube_search(query:str,max_results:int=3):
    api_key="AIzaSyAGvi5GGaF5f-UjfOM0S5DOYOg8PkBTOA4"
    endpoint="https://www.googleapis.com/youtube/v3/search"
    params=dict(
        key=api_key,
        q=query,
        part="snippet",
        type="video",
        maxResults=max_results*5,
    )
    resp=requests.get(endpoint,params=params,timeout=20).json()
    return[
       {
         "title":item["snippet"]["title"],
          "url": f"https://youtu.be/{item['id']['videoId']}",

         "channel":item["snippet"]["channelTitle"],  

       }
       for item in resp.get("items",[])
       if item.get("id", {}).get("kind") == "youtube#video" 
    ]

# Define the agent
root_agent = Agent(
    model="gemini-2.0-flash-001",
    name="stock_agent",
    description="RoadMap Generator",
   instruction = "You are a roadmap generator.The user will be giving a huge topic your goal is to split the huge topic into modules right from basics to advanced and for each module use the youtube_search function to generate the youtube links and return the roadmap with the modules listed in order along with the corresspoding youtube links and generate the roadmap with links returned"
,
    tools=[youtube_search]

)

# Create the runner
runner = Runner(
    app_name="agent",
    agent=root_agent,
    session_service=InMemorySessionService(),
    
)