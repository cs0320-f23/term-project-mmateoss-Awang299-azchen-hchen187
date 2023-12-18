import React, { useEffect, useState } from "react";
import MicRecorder from "mic-recorder-to-mp3";


const recorder = new MicRecorder({ bitRate: 128 });

export default function LyricsAudio() {
  const [isRecording, setIsRecording] = useState(false);
  const [blobURL, setBlobURL] = useState("");

  const handleRecordingClick = () => {
    if (!isRecording) {
        //start recording
        recorder
          .start()
          .then(() => {
            setIsRecording(true);
          })
          .catch((e) => console.error(e));
      } else {
        //stop recording
        recorder
        .stop()
        .getMp3()
        .then(([buffer, blob]) => {
          setBlobURL(URL.createObjectURL(blob));
          setIsRecording(false);
        })
        .catch((e) => {
          alert("Failed to stop recording");
          console.error(e);
        });
      }
  }

  const blobUrlToByteArray = async (blobUrl: string): Promise<Int8Array> => {
    const response = await fetch(blobUrl);
    const blob = await response.blob();
    return new Promise((resolve) => {
      const reader = new FileReader();
      reader.onloadend = function (event) {
        if (event.target?.result instanceof ArrayBuffer) {
          const byteArray = new Int8Array(event.target.result);
          resolve(byteArray);
        } else {
          throw new Error("Invalid result type");
        }
      };
      reader.readAsArrayBuffer(blob);
    });
  };
  
  const handleSubmitRecording = () => {
    blobUrlToByteArray(blobURL)
      .then((byteArray) => {
        console.log("byteArray:", byteArray);
  
        const token = "a06f3c6571d5817664b8e771a11cb07116eddbe5";
        const uriString = "http://localhost:3232/audioText?token=" + token;
        const headers: Record<string, string> = {
            'Accept': 'application/json',
            'Content-Type': 'audio/mpeg',
          };
  
        // Create a FormData object and append the audio file
        const formData = new FormData();
        formData.append("audio", new Blob([byteArray], { type: "audio/mpeg" }), "audio.mp3");
  
        // Make the fetch request
        fetch(uriString, {
          method: "POST",
          body: formData,
          headers: headers,
        })
          .then((response) => response.json())
          .then((data) => {
            console.log("Success:", data);
          })
          .catch((error) => {
            console.error("Error:", error);
          });
      })
      .catch((error) => {
        console.error("Error fetching or converting blob:", error);
      });
  };

//   const blobToBase64 = (url) => {
//     return new Promise<string>(async (resolve, _) => {
//       // do a request to the blob uri
//       const response = await fetch(url);
  
//       // response has a method called .blob() to get the blob file
//       const blob = await response.blob();
  
//       // instantiate a file reader
//       const fileReader = new FileReader();
  
//       // read the file
//       fileReader.readAsDataURL(blob);
  
//       fileReader.onloadend = function(){
//         if (typeof fileReader.result == "string") {
//             resolve(fileReader.result); // Here is the base64 string
//         }
//       }
//     });
//   };

//   const handleSubmitRecording = () => {
//     blobToBase64(blobURL)
//         .then(base64String => {
//         console.log(base64String)

//         const token = "a06f3c6571d5817664b8e771a11cb07116eddbe5";
//         const uriString = "http://localhost:3232/audioText?token=" + token;
//         const base64Content = base64String.split(',')[1];
        
//         // Convert base64 string to a Uint8Array
//         const byteArray = Uint8Array.from(atob(base64Content), (c) =>
//           c.charCodeAt(0)
//         );

//         console.log("bytearray: " + byteArray)

//         // Create a FormData object and append the audio file
//         const formData = new FormData();
//         formData.append(
//           "audio",
//           new Blob([byteArray], { type: "audio/mpeg" }),
//           "audio.mp3"
//         );

//         // Make the fetch request
//         fetch(uriString, {
//           method: "POST",
//           body: formData,
//           headers: {
//             Accept: "application/json",
//           },
//         })
//           .then((response) => response.json())
//           .then((data) => {
//             console.log("Success:", data);
//           })
//           .catch((error) => {
//             console.error("Error:", error);
//           });


//     });
    

    // if (blobURL) {
    //     fetch(blobURL)
    //       .then(response => response.blob())
    //       .then(blob => {
    //         const reader = new FileReader();
    //         reader.onloadend = () => {
    //             if (typeof reader.result === 'string') {
    //                 const base64Data = reader.result.split(',')[1];
    //                 // Now you have the base64 data, you can send it to your API
    //                 getAudioToText(base64Data);
    //               } else {
    //                 console.error('Error reading blob data');
    //               }
    //         };
    //         reader.readAsDataURL(blob);
    //       })
    //       .catch(error => {
    //         console.error('Error fetching blob:', error);
    //       });
    //   }

  //}

  const getAudioToText = (base64Data) => {
    console.log(base64Data)
  }

//   const startRecording = () => {
//     if (!isRecording) {
//       recorder
//         .start()
//         .then(() => {
//           setIsRecording(true);
//         })
//         .catch((e) => console.error(e));
//     }
//   };

//   const stopRecording = () => {
//     if (isRecording) {
//       recorder
//         .stop()
//         .getMp3()
//         .then(([buffer, blob]) => {
//           setBlobURL(URL.createObjectURL(blob));
//           setIsRecording(false);
//         })
//         .catch((e) => {
//           alert("Failed to stop recording");
//           console.error(e);
//         });
//     }
//   };

  return (
    <div className="App">
      <header className="App-header">
        <button onClick={handleRecordingClick}>
            {isRecording ? 'Stop Recording' : 'Start Recording'}
        </button>
        {/* <button onClick={startRecording} disabled={isRecording}>
          Record
        </button>
        <button onClick={stopRecording} disabled={!isRecording}>
          Stop
        </button> */}
        {blobURL && <audio src={blobURL} controls={true} />}
        <button onClick={handleSubmitRecording}>
            Submit
        </button>
      </header>
    </div>
  );
}
