# SAP SuccessFactors Connection

Description
-----------
Use this connection to access data in SAP SuccessFactors.

Properties
----------
**Name:** Name of the connection. Connection names must be unique in a namespace.

**Description:** Description of the connection.

**Authentication Type:** Authentication type used to submit request. Supported types are Basic & OAuth 2.0. Default is Basic Authentication.
* **Basic Authentication**  
  **SAP SuccessFactors Logon Username (M)**: SAP SuccessFactors Logon Username for user authentication.  
  **SAP SuccessFactors Logon Password (M)**: SAP SuccessFactors Logon password for user authentication.
* **OAuth 2.0**  
  **Client Id:** Client Id required to generate the token.  
  **Company Id:** Company Id required to generate the token.  
  **Assertion Token Type:** Assertion token can be entered or can be created using the required parameters.
* **Enter Token**  
  **Assertion Token:** Assertion token used to generate the access token.
* **Create Token**  
  **Token URL:** Token URL to generate the assertion token.  
  **Private Key:** Private key required to generate the token.  
  **User Id:** User Id required to generate the token.

**SAP SuccessFactors Base URL (M)**: SAP SuccessFactors Base URL.


Path of the connection
----------------------
To browse, get a sample from, or get the specification for this connection.  
/{entity} This path indicates a SuccessFactors entity. A entity is the only one that can be sampled.