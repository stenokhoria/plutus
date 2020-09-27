package main

import (
    "golang.org/x/crypto/ssh"
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
    config := &ssh.ClientConfig{
        User: "thom",
        Auth: []ssh.AuthMethod{
            // Use a callback rather than PublicKeys so we only
            // consult the
            // agent once the remote server wants it.
            ssh.PublicKeysCallback(agentClient.Signers),
        },
        HostKeyCallback: ssh.InsecureIgnoreHostKey(),
    }

    sshc, err := ssh.Dial("tcp", "localhost:22", config)
    if err != nil {
        log.Fatalf("Failed to connect: %v", err)
    }
    // Use sshc...
    sshc.Close()
}
