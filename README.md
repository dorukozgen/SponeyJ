# 🎵 SponeyJ - Spotify Stream Manager

<p align="center">
  <img src="src/main/resources/static/images/logo.png" alt="SponeyJ Logo" width="200"/>
</p>

An advanced Spotify stream management solution designed to enhance artists' streaming metrics through automated listening sessions. This client-based application utilizes Selenium automation and proxy integration for efficient stream management, providing a robust platform for managing multiple streaming instances.

## 🚀 Key Features

- 🌐 Advanced proxy support (HTTP/SOCKS)
- 🤖 Selenium-based automation
- 💻 Modern JavaFX interface
- 🔄 Multi-threading architecture
- 👥 Multiple account management
- 📊 Real-time streaming statistics
- ⚡ WebSocket integration
- 🔒 Secure client-based operations

## 🛠️ Requirements

- Java 17+
- Maven
- Selenium
- Chrome WebDriver
- WebSocket
- JavaFX
- Spring Boot

## 🔧 Installation & Running

### 1. Clone the repository:

```bash
git clone https://github.com/dorukozgen/sponeyj.git
```

### 2. Navigate to the project directory:

```bash
cd sponeyj
```

### 3. Build the project:

```bash
mvn clean install
```

### 4. Run the application:

Add your proxies to `config/proxies.txt`:

```
host:port:username:password
```

Add your accounts to `config/accounts.txt`:

```
email:password
```

Run the application:
```bash
mvn exec:java
```


## 📜 License

[MIT](LICENSE)

## 👤 Developer

Doruk Ozgen - [@dorukozgen](https://github.com/dorukozgen)

Project Link: [https://github.com/dorukozgen/SponeyJ](https://github.com/dorukozgen/SponeyJ)




