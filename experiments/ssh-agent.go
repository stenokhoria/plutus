package main

import (
    "golang.org/x/crypto/ssh/agent"
    "log"
    "net"
    "os"
)

func main() {
    // ssh-agent(1) provides a UNIX socket at $SSH_AUTH_SOCK.
    socket := os.Getenv("SSH_AUTH_SOCK")
    conn, err := net.Dial("unix", socket)
    if err != nil {
        log.Fatalf("Failed to open SSH_AUTH_SOCK: %v", err)
    }

    agentClient := agent.NewClient(conn)
    keys, err := agentClient.List()
    if err != nil {
        log.Fatalf("Failed to retrieve SSH public keys: %v", err)
    }
    for _, key := range keys {
        log.Printf("SSH key: %s", key.Comment)
    }
}
