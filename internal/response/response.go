package response

// R 统一 API 响应
type R[T any] struct {
	Code int    `json:"code"`
	Msg  string `json:"msg"`
	Data T      `json:"data,omitempty"`
}

func OK[T any](data T) R[T] {
	return R[T]{Code: 0, Msg: "成功", Data: data}
}

func Fail(code int, msg string) R[any] {
	return R[any]{Code: code, Msg: msg}
}

// PageResult 分页结果
type PageResult[T any] struct {
	Current int64 `json:"current"`
	Size    int64 `json:"size"`
	Total   int64 `json:"total"`
	Records []T   `json:"records"`
}
