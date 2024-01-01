package cn.vlts.mcp.exception;

/**
 * MCP异常
 *
 * @author throwable
 * @version v1
 * @description MCP异常
 * @since 2023/12/20 20:07
 */
public class McpException extends RuntimeException {

    public McpException(String message) {
        super(message);
    }

    public McpException(String message, Throwable cause) {
        super(message, cause);
    }

    public McpException(Throwable cause) {
        super(cause);
    }
}
