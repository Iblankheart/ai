<template>
	<view class="u-upload" :style="[$u.addStyle(customStyle)]">
		<view class="u-upload__wrap" :class="'u-upload__wrap__'+accept" >
			<template v-if="previewImage">
				<view
				    class="u-upload__wrap__preview"
				    v-for="(item, index) in lists"
				    :key="index"
				>
					<image
					    v-if="item.isImage || (item.type && item.type === 'image')"
					    :src="item.thumb || item.url"
					    :mode="imageMode"
					    class="u-upload__wrap__preview__image"
					    @tap="onPreviewImage(item)"
						:style="[{
							width: $u.addUnit(width),
							height: $u.addUnit(height)
						}]"
					/>
					<view
					    v-else
					    class="u-upload__wrap__preview__other"
					>
						<u-icon
						    color="#80CBF9"
						    size="26"
						    :name="item.isVideo || (item.type && item.type === 'video') ? 'movie' : 'folder'"
						></u-icon>
						<text class="u-upload__wrap__preview__other__text  text-cut">{{item.name||item.url}}</text>
					</view>
					<view
					    class="u-upload__status"
					    v-if="item.status === 'uploading' || item.status === 'failed'"
					>
						<view class="u-upload__status__icon">
							<u-icon
							    v-if="item.status === 'failed'"
							    name="close-circle"
							    color="#ffffff"
							    size="25"
							/>
							<u-loading v-else size="25"  mode="flower"  color="#ffffff"></u-loading>
						</view>
						<text
						    v-if="item.message"
						    class="u-upload__status__message"
						>{{ item.message }}</text>
					</view>
					<view
					    class="u-upload__deletable"
					    v-if="item.status !== 'uploading' && (deletable || item.deletable) &&!disabled"
					    @tap.stop="deleteItem(index)"
					>
						<view class="u-upload__deletable__icon">
							<u-icon
							    name="close"
							    color="#ffffff"
							    size="10"
							></u-icon>
						</view>
					</view>
				</view>
				
			</template>
			
			<template v-if="isInCount&&!disabled">
				<view
				    v-if="$slots.default || $slots.$default"
				    @tap="chooseFile"
				>
					<slot />
				</view>
				<view
				    v-else
				    class="u-upload__button"
				    :hover-class="!disabled ? 'u-upload__button--hover' : ''"
				    hover-stay-time="150"
				    @tap="chooseFile"
				    :class="[disabled && 'u-upload__button--disabled']"
					:style="[{
						width: $u.addUnit(width),
						height: $u.addUnit(height)
					}]"
				>
					<u-icon
					    :name="uploadIcon"
					    size="26"
					    :color="uploadIconColor"
					></u-icon>
					<text
					    v-if="uploadText"
					    class="u-upload__button__text"
					>{{ uploadText }}</text>
				</view>
			</template>
		</view>

	</view>
</template>

<script>
	import {
		chooseFile
	} from './utils';
	import mixin from './mixin.js';
	import props from './props.js';

	/**
	 * upload 上传
	 * @description 该组件用于上传图片场景
	 * @tutorial https://uviewui.com/components/upload.html
	 * @property {String}			accept				接受的文件类型, 可选值为all media image file video （默认 'image' ）
	 * @property {String | Array}	capture				图片或视频拾取模式，当accept为image类型时设置capture可选额外camera可以直接调起摄像头（默认 ['album', 'camera'] ）
	 * @property {Boolean}			compressed			当accept为video时生效，是否压缩视频，默认为true（默认 true ）
	 * @property {String}			camera				当accept为video时生效，可选值为back或front（默认 'back' ）
	 * @property {Number}			maxDuration			当accept为video时生效，拍摄视频最长拍摄时间，单位秒（默认 60 ）
	 * @property {String}			uploadIcon			上传区域的图标，只能内置图标（默认 'camera-fill' ）
	 * @property {String}			uploadIconColor		上传区域的图标的字体颜色，只能内置图标（默认 #D3D4D6 ）
	 * @property {Boolean}			useBeforeRead		是否开启文件读取前事件（默认 false ）
	 * @property {Boolean}			previewFullImage	是否显示组件自带的图片预览功能（默认 true ）
	 * @property {String | Number}	maxCount			最大上传数量（默认 52 ）
	 * @property {Boolean}			disabled			是否启用（默认 false ）
	 * @property {String}			imageMode			预览上传的图片时的裁剪模式，和image组件mode属性一致（默认 'aspectFill' ）
	 * @property {String}			name				标识符，可以在回调函数的第二项参数中获取
	 * @property {Array}			sizeType			所选的图片的尺寸, 可选值为original compressed（默认 ['original', 'compressed'] ）
	 * @property {Boolean}			multiple			是否开启图片多选，部分安卓机型不支持 （默认 false ）
	 * @property {Boolean}			deletable			是否展示删除按钮（默认 true ）
	 * @property {String | Number}	maxSize				文件大小限制，单位为byte （默认 Number.MAX_VALUE ）
	 * @property {Array}			fileList			显示已上传的文件列表
	 * @property {String}			uploadText			上传区域的提示文字
	 * @property {String | Number}	width				内部预览图片区域和选择图片按钮的区域宽度（默认 80 ）
	 * @property {String | Number}	height				内部预览图片区域和选择图片按钮的区域高度（默认 80 ）
	 * @property {Object}			customStyle			组件的样式，对象形式
	 * @event {Function} afterRead		读取后的处理函数
	 * @event {Function} beforeRead		读取前的处理函数
	 * @event {Function} oversize		文件超出大小限制
	 * @event {Function} clickPreview	点击预览图片
	 * @event {Function} delete 		删除图片
	 * @example <u-uploads :action="action" :fileList="fileList" ></u-uploads>
	 */
	export default {
		name: "u-upload",
		mixins: [mixin,props],
		emits: ["update:file-list", "on-remove", "on-success", "on-change", "on-error","update:modelValue"],
		data() {
			return {
				// #ifdef APP-NVUE
				successIcon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAKKADAAQAAAABAAAAKAAAAAB65masAAACP0lEQVRYCc3YXygsURwH8K/dpcWyG3LF5u/6/+dKVylSypuUl6uUPMifKMWL8oKEB1EUT1KeUPdR3uTNUsSLxb2udG/cbvInNuvf2rVnazZ/ZndmZ87snjM1Z+Z3zpzfp9+Z5mEAhlvjRtZgCKs+gnPAOcAkkMOR4jEHfItjDvgRxxSQD8cM0BuOCaAvXNCBQrigAsXgggYUiwsK0B9cwIH+4gIKlIILGFAqLiBAOTjFgXJxigJp4BQD0sIpAqSJow6kjSNAFTnRaHJwLenD6Mud52VQAcrBfTd2oyq+HtGaGGWAcnAVcXWoM3bCZrdi+ncPfaAcXE5UKVpdW/vitGPqqAtn98d0gXJwX7Qp6MmegUYVhvmTIezdmHlxJCjpHRTCFerLkRRu4k0aqdajN3sWOo0BK//msHa+xDuPC/oNFMKRhTtM4xjIX0SCNpXL4+7VIaHuyiWEp2L7ahWLf8fejfPdqPmC3mJicORZUp1CQzm+GiphvljGk+PBvWRbxii+xVTj5M6CiZ/tsDufvaXyxEUDxeLIyvu3m0iOyEFWVAkydcVYdyFrE9tQk9iMq6f/GNlvwt3LjQfh60LUrw9/cFyyMJUW/XkLSNMV4Mi6C5ML+ui4x5ClAX9sB9w0wV6wglJwJCv5fOxcr6EstgbGiEw4XcfUry4cWrcEUW8n+ARKxXEJHhw2WG43UKSvwI/TSZgvl7kh0b3XLZaLEy0QmMgLZAVH7J+ALOE+AVnDvQOyiPMAWcW5gSzjCPAV+78S5WE0GrQAAAAASUVORK5CYII=',
				// #endif
				lists: [],
				isInCount: true,
			}
		},
		computed: {
			valueCom() {
				// #ifndef VUE3
				return this.value;
				// #endif
			
				// #ifdef VUE3
				return this.modelValue;
				// #endif
			}
		},
		watch: {
			valueCom(nVal, oVal) {
				if(nVal){
					if(nVal!=oVal){
						this.initValue(nVal)
					}
				}else{
					this.lists = []
				}
			},
			// 监听文件列表的变化，重新整理内部数据
			fileList(val){
				this.formatFileList()
			}
		},
		created() {
			this.initValue(this.valueCom)
		},
		methods: {
			formatFileList() {
				const {
					fileList = [], maxCount
				} = this;
				const lists = fileList.map((item) =>
					Object.assign(Object.assign({}, item), {
						// 如果item.url为本地选择的blob文件的话，无法判断其为video还是image，此处优先通过accept做判断处理
						isImage: this.accept === 'image' || uni.$u.test.image(item.url || item.thumb),
						isVideo: this.accept === 'video' || uni.$u.test.video(item.url || item.thumb),
						deletable: typeof(item.deletable) === 'boolean' ? item.deletable : this.deletable,
					})
				);
				this.lists = lists
				this.isInCount = lists.length < maxCount
			},
			initValue(nVal){
				if(!nVal){
					this.lists = []
				}else{
					this.lists = nVal.split(",").map(item=>{
						return {url:item}
					})
				}
				this.isInCount = this.lists.length < this.maxCount
			},
			chooseFile() {
				const {
					maxCount,
					multiple,
					lists,
					disabled
				} = this;
				if (disabled) return;
				// 如果用户传入的是字符串，需要格式化成数组
				let capture;
				try {
					capture = uni.$u.test.array(this.capture) ? this.capture : this.capture.split(',');
				}catch(e) {
					capture = [];
				}
				chooseFile(
						Object.assign({
							accept: this.accept,
							extension:this.extension,
							multiple: this.multiple,
							capture: capture,
							compressed: this.compressed,
							maxDuration: this.maxDuration,
							sizeType: this.sizeType,
							camera: this.camera,
						}, {
							maxCount: maxCount - lists.length,
						})
					)
					.then((res) => {
						this.onBeforeRead(multiple ? res : res[0]);
					})
					.catch((error) => {
						this.$emit('on-error', error);
					});
			},
			// 文件读取之前
			onBeforeRead(file) {
				// 检查上传地址
				if (!this.action) {
					this.showToast("请配置上传地址");
					return;
				}
				const {
					beforeRead,
					useBeforeRead,
				} = this;
				let res = true
				// beforeRead是否为一个方法
				if (uni.$u.test.func(beforeRead)) {
					// 如果用户定义了此方法，则去执行此方法，并传入读取的文件回调
					res = beforeRead(file, this.getDetail());
				}
				if (useBeforeRead) {
					res = new Promise((resolve, reject) => {
						this.$emit(
							'beforeRead',
							Object.assign(Object.assign({
								file
							}, this.getDetail()), {
								callback: (ok) => {
									ok ? resolve() : reject();
								},
							})
						);
					});
				}
				if (!res) {
					return;
				}
				if (uni.$u.test.promise(res)) {
					res.then((data) => this.onAfterRead(data || file));
				} else {
					this.onAfterRead(file);
				}
			},
			getDetail(index) {
				return {
					name: this.name,
					index: index == null ? this.fileList.length : index,
				};
			},
			// 上传失败
			uploadError(index, err) {
				this.lists[index].status = 'failed';
				this.lists[index].message = '上传失败';
				this.showToast("上传失败，请重试");
			},
			// 新增图片
			async uploadFile(event) {
				// 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
				let lists = [].concat(event.file)
				let fileListLen = this.lists.length
				lists.map((item) => {
					this.lists.push({
						...item,
						status: 'uploading',
						message: '上传中'
					})
				})
				
				for (let i = 0; i < lists.length; i++) {
					const result = await this.uploadFilePromise(lists[i].url,i)
					if(result){
						let item = this.lists[fileListLen]
						this.lists.splice(fileListLen, 1, Object.assign(item, {
							status: 'success',
							message: '',
							url: result
						}))
						fileListLen++
					}
				}
				this.$emit("on-success", this.lists);
				this.changeValue( this.lists)
			},
			changeValue(lists){
				let datas = lists.map((item) => {
					return item.url;
				});
				this.$emit("update:modelValue", datas.join(','));
				this.isInCount = datas.length < this.maxCount
			},
			uploadFilePromise(url,index) {
				let thiz = this
				return new Promise((resolve, reject) => {
					let header = {}
					if(getApp().globalData.currentPage && getApp().globalData.currentPage.$session){
						header.Authorization =  getApp().globalData.currentPage.$session.getToken()||''
					}
					let a = uni.uploadFile({
						url: getApp().globalData.currentPage && getApp().globalData.currentPage.$http?getApp().globalData.currentPage.$http.setUrl(thiz.action,{}):thiz.action, // 仅为示例，非真实的接口地址
						filePath: url,
						name: 'file',
						header: header,
						formData: {
							type: this.accept
						},
						success: (res) => {
							let data = this.$u.test.jsonString(res.data) ? JSON.parse(res.data) : res.data;
							// setTimeout(() => {
							data = data.data
							if(!data.url){
								console.log("请返回正确结构数据比如:{code:200,data:{url:'图片路径'}}")
								thiz.uploadError(index, {});
								reject(false);
							}
							if(data.tip){
								resolve(data.tip)
							}else{
								let url = getApp().globalData.currentPage && getApp().globalData.currentPage.$tools? getApp().globalData.currentPage.$tools.renderImage(data.url):data.url
								resolve(url)
							}
							// }, 1000)
						},
						fail: e => {
							thiz.uploadError(index, e);
						}
					});
				})
			},
			onAfterRead(file) {
				const {
					maxSize,
					afterRead
				} = this;
				const oversize = Array.isArray(file) ?
					file.some((item) => item.size > maxSize) :
					file.size > maxSize;
				if (oversize) {
					this.$emit('oversize', Object.assign({
						file
					}, this.getDetail()));
					this.showToast("超出允许的文件大小");
					return;
				}
				if (typeof afterRead === 'function') {
					afterRead(file, this.getDetail());
				}
				let data ={file:file}
				this.uploadFile(data)
				// this.$emit('afterRead', Object.assign({
				// 	file
				// }, this.getDetail()));
			},
			// 删除一个图片
			deleteItem(index) {
				uni.showModal({
					title: "提示",
					content: "您确定要删除此项吗？",
					success: async res => {
						if (res.confirm) {
							// 如果不存在before-remove钩子，
							this.handlerDeleteItem(index);
						}
					}
				});
			},
			// 执行移除图片的动作，上方代码只是判断是否可以移除
			handlerDeleteItem(index) {
				this.lists.splice(index, 1);
				this.$forceUpdate();
				this.$emit("on-remove", this.lists);
				this.changeValue(this.lists)
				//this.showToast('移除成功');
			},
			// 预览图片
			onPreviewImage(item) {
				if (!this.previewFullImage) return
				uni.previewImage({
					// 先filter找出为图片的item，再返回filter结果中的图片url
					urls: this.lists.filter((item) => this.accept === 'image' || uni.$u.test.image(item.url || item.thumb)).map((item) => item.url || item.thumb),
					current: item.url || item.thumb,
					fail() {
						uni.$u.toast('预览图片失败')
					},
				});
			},
			onPreviewVideo(event) {
				if (!this.data.previewFullImage) return;
				const {
					index
				} = event.currentTarget.dataset;
				const {
					lists
				} = this.data;
				wx.previewMedia({
					sources: lists
						.filter((item) => isVideoFile(item))
						.map((item) =>
							Object.assign(Object.assign({}, item), {
								type: 'video'
							})
						),
					current: index,
					fail() {
						uni.$u.toast('预览视频失败')
					},
				});
			},
			onClickPreview(event) {
				const {
					index
				} = event.currentTarget.dataset;
				const item = this.data.lists[index];
				this.$emit(
					'clickPreview',
					Object.assign(Object.assign({}, item), this.getDetail(index))
				);
			}
		}
	}
</script>

<style lang="scss" scoped>
	@import "../../libs/css/style.components.scss";
	$u-upload-preview-border-radius: 2px !default;
	$u-upload-preview-margin: 0 8px 8px 0 !default;
	$u-upload-image-width:80px !default;
	$u-upload-image-height:$u-upload-image-width;
	$u-upload-other-bgColor: rgb(242, 242, 242) !default;
	$u-upload-other-flex:1 !default;
	$u-upload-text-font-size:11px !default;
	$u-upload-text-color:$u-tips-color !default;
	$u-upload-text-margin-top:2px !default;
	$u-upload-deletable-right:0 !default;
	$u-upload-deletable-top:0 !default;
	$u-upload-deletable-bgColor:#fa3534 !default;
	$u-upload-deletable-height:20px !default;
	$u-upload-deletable-width:$u-upload-deletable-height;
	$u-upload-deletable-boder-bottom-left-radius:100px !default;
	$u-upload-deletable-zIndex:3 !default;
	$u-upload-success-bottom:0 !default;
	$u-upload-success-right:0 !default;
	$u-upload-success-border-style:solid !default;
	$u-upload-success-border-top-color:transparent !default;
	$u-upload-success-border-left-color:transparent !default;
	$u-upload-success-border-bottom-color: $u-success !default;
	$u-upload-success-border-right-color:$u-upload-success-border-bottom-color;
	$u-upload-success-border-width:9px !default;
	$u-upload-icon-top:0px !default;
	$u-upload-icon-right:0px !default;
	$u-upload-icon-h5-top:1px !default;
	$u-upload-icon-h5-right:0 !default;
	$u-upload-icon-width:16px !default;
	$u-upload-icon-height:$u-upload-icon-width;
	$u-upload-success-icon-bottom:-10px !default;
	$u-upload-success-icon-right:-10px !default;
	$u-upload-status-right:0 !default;
	$u-upload-status-left:0 !default;
	$u-upload-status-bottom:0 !default;
	$u-upload-status-top:0 !default;
	$u-upload-status-bgColor:rgba(0, 0, 0, 0.5) !default;
	$u-upload-status-icon-Zindex:1 !default;
	$u-upload-message-font-size:12px !default;
	$u-upload-message-color:#FFFFFF !default;
	$u-upload-message-margin-top:5px !default;
	$u-upload-button-width:80px !default;
	$u-upload-button-height:$u-upload-button-width;
	$u-upload-button-bgColor:rgb(244, 245, 247) !default;
	$u-upload-button-border-radius:2px !default;
	$u-upload-botton-margin: 0 8px 8px 0 !default;
	$u-upload-text-font-size:11px !default;
	$u-upload-text-color:$u-tips-color !default;
	$u-upload-text-margin-top: 2px !default;
	$u-upload-hover-bgColor:rgb(230, 231, 233) !default;
	$u-upload-disabled-opacity:.5 !default;

	.u-upload {
		@include vue-flex(column);
		flex: 1;
		overflow: hidden;
		.diygw-btn{
			padding:0 !important;
		}

		&__wrap {
			@include vue-flex;
			flex-wrap: wrap;
			flex: 1;
			
			&__preview {
				border-radius: $u-upload-preview-border-radius;
				margin: $u-upload-preview-margin;
				position: relative;
				overflow: hidden;
				@include vue-flex;

				&__image {
					width: $u-upload-image-width;
					height: $u-upload-image-height;
				}

				&__other {
					width: $u-upload-image-width;
					height: $u-upload-image-height;
					background-color: $u-upload-other-bgColor;
					flex: $u-upload-other-flex;
					@include vue-flex(column);
					justify-content: center;
					align-items: flex-start;
					

					&__text {
						font-size: $u-upload-text-font-size;
						color: $u-upload-text-color;
						margin-top: $u-upload-text-margin-top;
						text-align: left;
					}
				}
			}
			
			&__file,&__all{
				width: 100%;
				flex-direction: column-reverse;
				overflow: hidden;
				
				.u-upload__wrap__preview{
					margin-top:8px;
					margin-bottom: inherit;
					margin-right:0;
				}
				.diygw-btn{
					padding:0 !important;
				}
				.u-upload__wrap__preview__other{
					height:40px;
				}
			}
		}

		&__deletable {
			position: absolute;
			top: $u-upload-deletable-top;
			right: $u-upload-deletable-right;
			@include vue-flex;
			align-items: center;
			justify-content: center;
			z-index: $u-upload-deletable-zIndex;

			&__icon {
				display: flex;
				flex-direction: row;
				align-items: center;
				justify-content: center;
				border-radius: 50%;
				width: 36rpx;
				height: 36rpx;
				background-color: $u-upload-deletable-bgColor;
				position: absolute;
				top: 0px;
				right: 0px;
			}
		}

		&__success {
			position: absolute;
			bottom: $u-upload-success-bottom;
			right: $u-upload-success-right;
			@include vue-flex;
			// 由于weex(nvue)为阿里巴巴的KPI(部门业绩考核)的laji产物，不支持css绘制三角形
			// 所以在nvue下使用图片，非nvue下使用css实现
			/* #ifndef APP-NVUE */
			border-style: $u-upload-success-border-style;
			border-top-color: $u-upload-success-border-top-color;
			border-left-color: $u-upload-success-border-left-color;
			border-bottom-color: $u-upload-success-border-bottom-color;
			border-right-color: $u-upload-success-border-right-color;
			border-width: $u-upload-success-border-width;
			align-items: center;
			justify-content: center;
			/* #endif */

			&__icon {
				/* #ifndef APP-NVUE */
				position: absolute;
				transform: scale(0.7);
				bottom: $u-upload-success-icon-bottom;
				right: $u-upload-success-icon-right;
				/* #endif */
				/* #ifdef APP-NVUE */
				width: $u-upload-icon-width;
				height: $u-upload-icon-height;
				/* #endif */
			}
		}

		&__status {
			position: absolute;
			top: $u-upload-status-top;
			bottom: $u-upload-status-bottom;
			left: $u-upload-status-left;
			right: $u-upload-status-right;
			background-color: $u-upload-status-bgColor;
			@include vue-flex(column);
			align-items: center;
			justify-content: center;

			&__icon {
				position: relative;
				z-index: $u-upload-status-icon-Zindex;
			}

			&__message {
				font-size: $u-upload-message-font-size;
				color: $u-upload-message-color;
				margin-top: $u-upload-message-margin-top;
			}
		}

		&__button {
			@include vue-flex(column);
			align-items: center;
			justify-content: center;
			width: $u-upload-button-width;
			height: $u-upload-button-height;
			background-color: $u-upload-button-bgColor;
			border-radius: $u-upload-button-border-radius;
			margin: $u-upload-botton-margin;
			/* #ifndef APP-NVUE */
			box-sizing: border-box;
			/* #endif */

			&__text {
				font-size: $u-upload-text-font-size;
				color: $u-upload-text-color;
				margin-top: $u-upload-text-margin-top;
			}

			&--hover {
				background-color: $u-upload-hover-bgColor;
			}

			&--disabled {
				opacity: $u-upload-disabled-opacity;
			}
		}
	}
</style>
