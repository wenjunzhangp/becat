<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>BeCat-我喜欢撸猫喜欢宁静的生活</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="shortcut icon" href="/resource/images/favicon.ico" />
	<link rel="stylesheet" type="text/css" href="/resource/js/layui/css/layui.css" media="all">
	<link rel="stylesheet" href="/resource/css/public.css" media="all" />
</head>
<body class="childrenBody">
<form class="layui-form layui-row platEventForm">
	<input type="hidden" class="idval" name="id" value="">
	<div class="layui-col-xs12">
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">标题</label>
			<div class="layui-input-block">
				<input type="text" class="layui-input title" name="title" lay-verify="required" placeholder="请输入大事记标题">
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">状态</label>
			<div class="layui-input-block">
				<input type="checkbox" class="layui-input status" lay-text="启用|禁用" lay-filter="status" lay-skin="switch" />
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<label class="layui-form-label">详细描述</label>
			<div class="layui-input-block">
				<textarea name="content" id="content" class="content" ></textarea>
			</div>
		</div>
		<div class="layui-form-item layui-row layui-col-xs12">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="modifyPlatEvent">发布</button>
				<button type="reset" class="layui-btn layui-btn-primary">取消</button>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="/resource/js/layui/layui.js"></script>
<script type="text/javascript" src="/resource/js/manager/platEventModify.js"></script>
</body>
</html>