var Main = {
    data() {
        return {
            dialogImageUrl: '',
            dialogVisible: false
        };
    },
    methods: {
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        }
    }
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')


// // var Main = {
// //     data() {
// //         return {
// //             dialogImageUrl: '',
// //             dialogVisible: false
// //         };
// //     },
// //     methods: {
// //         handleRemove(file, fileList) {
// //             console.log(file, fileList);
// //         },
// //         handlePictureCardPreview(file) {
// //             this.dialogImageUrl = file.url;
// //             this.dialogVisible = true;
// //         }
// //     }
// // }
// // var Ctor = Vue.extend(Main)
// // new Ctor().$mount('#app')
// export default {
//     data() {
//         return {
//             dialogImageUrl: '',
//             dialogVisible: false,
//             productImgs: [],
//             isMultiple: true,
//             imgLimit: 6
//         }
//     },
//     methods: {
//         handleRemove(file, fileList) {//移除图片
//             console.log(file, fileList);
//         },
//         handlePictureCardPreview(file) {//预览图片时调用
//             console.log(file);
//             this.dialogImageUrl = file.url;
//             this.dialogVisible = true;
//         },
//
//         beforeAvatarUpload(file) {//文件上传之前调用做一些拦截限制
//             console.log(file);
//             const isJPG = true;
//             // const isJPG = file.type === 'image/jpeg';
//             const isLt2M = file.size / 1024 / 1024 < 2;
//
//             // if (!isJPG) {
//             //   this.$message.error('上传头像图片只能是 JPG 格式!');
//             // }
//             if (!isLt2M) {
//                 this.$message.error('上传图片大小不能超过 2MB!');
//             }
//             return isJPG && isLt2M;
//         },
//         handleAvatarSuccess(res, file) {//图片上传成功
//             console.log(res);
//             console.log(file);
//             this.imageUrl = URL.createObjectURL(file.raw);
//         },
//         handleExceed(files, fileList) {//图片上传超过数量限制
//             this.$message.error('上传图片不能超过6张!');
//             console.log(file, fileList);
//         },
//         imgUploadError(err, file, fileList) {//图片上传失败调用
//             console.log(err)
//             this.$message.error('上传图片失败!');
//         }
//     }
}