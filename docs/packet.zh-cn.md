### CPacket

所有接受到的包的母类

### CExecuteCommand

执行命令的数据包，结构为
```json
{
  "type" : "command",
  "data" : "{command_str}"
}
```
`{command_str}`为需要执行的指令

[SExecuteCommand](#SExecuteCommand)为对应的返回包