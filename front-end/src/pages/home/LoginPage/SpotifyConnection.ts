

// Code verifier
const generateRandomString = (length: number) => {
  const possible =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  const values = crypto.getRandomValues(new Uint8Array(length));
  return values.reduce((acc, x) => acc + possible[x % possible.length], "");
};


// Code Challenge
const sha256 = async (plain: string) => {
  const encoder = new TextEncoder();
  const data = encoder.encode(plain);
  return window.crypto.subtle.digest("SHA-256", data);
};

const base64encode = (input: ArrayBuffer) => {
  return btoa(String.fromCharCode(...new Uint8Array(input)))
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
};


// Request User Authorization
const clientId = "62f77f3bf89c4d9eacd7276d2255e4d0";
const redirectUri = "http://localhost:8000/";

const scope = "user-read-private user-read-email";



// get code
const urlParams = new URLSearchParams(window.location.search);
let code = urlParams.get("code");


export async function spotifyLogin(){
  const codeVerifier = generateRandomString(64);
  const hashed = await sha256(codeVerifier);
  const codeChallenge = base64encode(hashed);
  window.localStorage.setItem("code_verifier", codeVerifier);
  const authUrl = new URL("https://accounts.spotify.com/authorize");

  const params = {
    response_type: "code",
    client_id: clientId,
    scope,
    code_challenge_method: "S256",
    code_challenge: codeChallenge,
    redirect_uri: redirectUri,
  };

  authUrl.search = new URLSearchParams(params).toString();
  window.location.href = authUrl.toString();
  const resp = getToken(code);
  console.log(resp);
}


// request access token
async function getToken( code: any){
  // stored in the previous step
  let codeVerifier= localStorage.getItem("code_verifier");
  let response: Response;
  if(typeof codeVerifier == "string"){
    response = await fetch("https://accounts.spotify.com/api/token", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        client_id: clientId,
        grant_type: 'authorization_code',
        code: code,
        redirect_uri: redirectUri,
        code_verifier: codeVerifier,
      }),
    });

    return await response.json();
  }
};





