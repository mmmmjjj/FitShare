<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvPublisherVideo',

	props: {
		streamManager: Object,
	},

	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>

<style scoped>
video {
	width: 160px;
	margin: 5px 30px;
	border-radius: 10px;
	cursor: pointer;
}
</style>