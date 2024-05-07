<template>
	<view class="container container32674">
		<view class="flex diygw-col-24 diygw-bottom bar-clz">
			<view class="diygw-grid diygw-actions">
				<button class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 图像识别 </view>
					</view>
				</button>
				<button class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/3wzsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本识别 </view>
					</view>
				</button>
				<button class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/wbjc.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本纠错 </view>
					</view>
				</button>
				<button class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txdmh-copy.png"></image>
						</view>
						<view class="diygw-grid-title"> 人像动漫风 </view>
					</view>
				</button>
			</view>
		</view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview1-clz">
			<view class="diygw-absolute autoview1_0">
				<image src="/static/e05b-kphwumr4958710.png" class="response diygw-col-24" mode="widthFix"></image>
			</view>
		</view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview-clz">
			<view class="diygw-absolute autoview_0">
				<view class="diygw-col-24 text-clz"> 图像识别 </view>
			</view>
		</view>


		<u-form :model="form" :rules="formRules" :errorType="['message', 'toast']" ref="formRef"
			class="flex diygw-form diygw-col-24">
			<u-form-item :borderBottom="false" :required="true" class="diygw-col-24" label="上传图片" labelPosition="top"
				prop="upload">
				<u-upload width="160" height="160" :before-upload="beforeUpload" action="http://localhost:8080/img" ref="uploadRef">
				</u-upload>
			</u-form-item>
			<view @click="uploadFileToServer" class="diygw-col-24 bg-none text4-clz"> 提交 </view>
		</u-form>












		<view class="flex diygw-col-24 diygw-bottom bar1-clz">
			<view class="diygw-grid diygw-actions">
				<button @tap="navigateTo" data-type="page" data-url="/pages/img/img" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 图像识别 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/text/text" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/3wzsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本识别 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/nlp/nlp" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/wbjc.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本纠错 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/cartoon/cartoon" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txdmh-copy.png"></image>
						</view>
						<view class="diygw-grid-title"> 人像动漫风 </view>
					</view>
				</button>
			</view>
		</view>
		<view class="diygw-col-24 text1-clz"> 识别结果: </view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview2-clz">
			<view class="diygw-absolute autoview2_0">
				<view class="diygw-col-24 text2-clz" v-if="showRecognitionResult">{{ recognitionResult }}</view>
			</view>
		</view>
		<view class="clearfix"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					upload: [] // 用于存放上传的图片
				},
				formRules: {
					upload: [{
						required: true,
						message: '请上传图片',
						trigger: 'change'
					}]
				},
				uploadFile: [],
				recognitionResult: '', // 用于存放识别结果
				showRecognitionResult: false
			};
		},
		onShow() {
			this.setCurrentPage(this);
		},

		onReady() {
			this.$refs.formRef?.setRules(this.formRules);
		},
		methods: {
			 beforeUpload(index, list) {
			   console.log("执行");
			   
			   // 获取上传的图片文件
			   const file = list[index].file;
			   console.log(file);
			  
			   console.log(this.$refs.uploadRef.lists[0]);
			   
			   // 创建 FormData 对象
			   let formData = new FormData();
			   formData.append('file', this.$refs.uploadRef.lists[0].file); // 使用 file.raw 获取原始文件对象
			   // 使用 get 方法获取 'file' 字段的值
			   let fileValue = formData.get('file');
			   console.log('file', fileValue);
			   console.dir(fileValue);
			   // 发送请求给后端进行图像识别
			   return new Promise((resolve, reject) => {
			     // 替换 'url' 为实际的后端接口地址
			     uni.uploadFile({
			       url: 'http://localhost:8080/img', // 修改为你的后端接口地址
			       filePath: file.path, // 上传文件路径
			       name: 'file', // 后端接收文件的字段名
			       formData: formData, // 表单数据
			       success: (res) => {
			         console.log("成功");
			         // 解析后端返回的数据
			         let data = JSON.parse(res.data);
					 console.log(data);
			         // 将识别结果作为 resolve 的参数
			         resolve(data.result);
			       },
			       fail: (err) => {
			         console.log("出错");
			         // 中断上传，并将错误信息作为 reject 的参数
			         reject(err);
			       }
			     });
			   });
			 }

		}
	};
</script>

<style lang="scss" scoped>
	.bar-clz {
		font-size: 26rpx !important;
	}

	.autoview1-clz {
		z-index: 1000;
		height: 360rpx;
	}

	.autoview1_0 {
		left: 58rpx;
		top: 16rpx;
		width: 638rpx;
		height: 336rpx;
		z-index: 0;
	}

	.autoview1_0>.flex {
		height: 100%;
	}

	.autoview-clz {
		z-index: 1000;
		height: 120rpx;
	}

	.autoview_0 {
		left: 126rpx;
		top: 16rpx;
		width: 456rpx;
		height: 118rpx;
		z-index: 0;
	}

	.autoview_0>.flex {
		height: 100%;
	}

	.text-clz {
		font-size: 64rpx !important;
		text-align: center;
	}

	.text4-clz {
		padding-top: 20rpx;
		border-bottom-left-radius: 120rpx;
		color: #ffffff;
		font-weight: bold;
		letter-spacing: 6rpx !important;
		padding-left: 10rpx;
		font-size: 32rpx !important;
		padding-bottom: 20rpx;
		border-top-right-radius: 120rpx;
		margin-right: 30rpx;
		background-color: #2a928f;
		margin-left: 30rpx;
		box-shadow: 0rpx 6rpx 14rpx rgba(64, 195, 3, 0.35);
		overflow: hidden;
		width: calc(100% - 30rpx - 30rpx) !important;
		border-top-left-radius: 120rpx;
		margin-top: 10rpx;
		border-bottom-right-radius: 120rpx;
		margin-bottom: 10rpx;
		text-align: center;
		padding-right: 10rpx;
	}

	.bar1-clz {
		font-size: 26rpx !important;
	}

	.text1-clz {
		font-size: 44rpx !important;
	}

	.autoview2-clz {
		z-index: 1000;
		height: 400rpx;
	}

	.autoview2_0 {
		left: 88rpx;
		top: 48rpx;
		width: 662rpx;
		height: 288rpx;
		z-index: 0;
	}

	.autoview2_0>.flex {
		height: 100%;
	}

	.text2-clz {
		font-size: 36rpx !important;
	}

	.container32674 {
		padding-left: 0px;
		padding-right: 0px;
	}

	.container32674 {
		padding-bottom: 80px;
	}
</style>
